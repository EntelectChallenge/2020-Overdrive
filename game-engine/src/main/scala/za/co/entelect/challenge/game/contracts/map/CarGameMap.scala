package za.co.entelect.challenge.game.contracts.map

import java.util

import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.common.RefereeMessage
import za.co.entelect.challenge.game.contracts.game.{CarGamePlayer, GamePlayer}
import za.co.entelect.challenge.game.contracts.player.Player

import scala.collection.mutable

class CarGameMap(players: util.List[Player], mapGenerationSeed: Int, lanes: Int, trackLength: Int, var blocks: Array[Block], var round: Int) extends GameMap {

    var stagedFuturePositions: List[StagedPosition] = List[StagedPosition]()
    var stagedCyberTruckPositions: List[StagedPosition] = List[StagedPosition]()

    override def getCurrentRound: Int = {
        return round
    }

    override def setCurrentRound(currentRound: Int): Unit = {
        round = currentRound
    }

    override def getWinningPlayer: GamePlayer = {
        val winningPlayers = mutable.ListBuffer[GamePlayer]()
        for (i <- 0 until players.size()) {
            val gamePlayer = players.get(i).getGamePlayer
            val carGamePlayer = gamePlayer.asInstanceOf[CarGamePlayer]
            if (carGamePlayer.getState() == Config.FINISHED_PLAYER_STATE) {
                winningPlayers.addOne(gamePlayer)
            }
        }

        if (winningPlayers.isEmpty) {
            return null
        }
        else if (winningPlayers.size == 1) {
            val firstPlayerAcrossTheLine = winningPlayers.head
            return firstPlayerAcrossTheLine
        }
        else {
            val winnersSortedBySpeed = winningPlayers.sortBy(x => x.asInstanceOf[CarGamePlayer].getSpeed())
            val winnersWithTheHighestSpeed = winnersSortedBySpeed.filter(x => x.asInstanceOf[CarGamePlayer].getSpeed() == winnersSortedBySpeed.last.asInstanceOf[CarGamePlayer].getSpeed())
            val fastestPlayerWithHighestScore = winnersWithTheHighestSpeed.maxBy(x => x.getScore)
            return fastestPlayerWithHighestScore
        }
    }

    override def getRefereeIssues: RefereeMessage = {
        throw new NotImplementedError("Car game map get referee issues")
    }

    def getMapFragment(gamePlayer: CarGamePlayer): CarGameMapFragment = {
        val gamePlayerId = gamePlayer.getGamePlayerId()
        val playerBlockPosition = getPlayerBlockPosition(gamePlayerId)
        val playerSpeed = gamePlayer.getSpeed()
        val playerState = gamePlayer.getState()
        val playerPowerUps = gamePlayer.getPowerups()
        val isBoosting = gamePlayer.isBoosting()
        val playerBoostCounter = gamePlayer.getBoostCounter()
        val score = gamePlayer.getScore()
        val lastCyberTruckPosition = gamePlayer.getCurrentCyberTruckPosition()
        val player = new MapFragmentPlayer(gamePlayerId, playerBlockPosition, playerSpeed, playerState, playerPowerUps,
            isBoosting, playerBoostCounter, score, lastCyberTruckPosition)

        val lanes = blocks.filter(block =>
            (((playerBlockPosition.getBlockNumber() >= block.getPosition().getBlockNumber()) && (scala.math.abs(playerBlockPosition.getBlockNumber() - block.getPosition().getBlockNumber()) <= Config.BACKWARD_VISIBILITY))
                || ((playerBlockPosition.getBlockNumber() <= block.getPosition().getBlockNumber()) && (scala.math.abs(block.getPosition().getBlockNumber() - playerBlockPosition.getBlockNumber()) <= Config.FORWARD_VISIBILITY))))

        val opponentGamePlayer = getCarGamePlayers().find(p => p.getGamePlayerId() != gamePlayer.getGamePlayerId()).get
        val opponentGamePlayerId = opponentGamePlayer.getGamePlayerId()
        val opponentBlock = getPlayerBlockPosition(opponentGamePlayerId)

        val opponent = new MapFragmentPlayer(opponentGamePlayerId, opponentBlock, opponentGamePlayer.getSpeed(), opponentGamePlayer.getState(),
            opponentGamePlayer.getPowerups(), opponentGamePlayer.isBoosting(), opponentGamePlayer.getBoostCounter(), opponentGamePlayer.getScore, opponentGamePlayer.getCurrentCyberTruckPosition())

        val carGameMapFragment = new CarGameMapFragment(round, player, opponent, lanes)
        return carGameMapFragment
    }

    def getPlayerBlockPosition(gameplayerId: Int): BlockPosition = {
        val indexOfPlayerOnTrack = blocks.indexWhere(x => x.occupiedByPlayerWithId == gameplayerId)
        val playerBlock = blocks(indexOfPlayerOnTrack)
        val playerBlockPosition = playerBlock.getPosition()
        return playerBlockPosition
    }

    def getCarGamePlayers(): Array[CarGamePlayer] = {
        var carGamePlayers = new Array[CarGamePlayer](players.size())
        for (i <- 0 until players.size()) {
            val player = players.get(i)
            val gamePlayer = player.getGamePlayer
            val carGamePlayer = gamePlayer.asInstanceOf[CarGamePlayer]
            carGamePlayers(i) = carGamePlayer
        }
        return carGamePlayers
    }

    def getGamePlayers(): Array[GamePlayer] = {
        var gamePlayers = new Array[GamePlayer](players.size())
        for (i <- 0 until players.size()) {
            val player = players.get(i)
            val gamePlayer = player.getGamePlayer
            gamePlayers(i) = gamePlayer
        }
        return gamePlayers
    }

    def vacateBlock(position: BlockPosition) = {
        val blockToVacate = getBlock(position)
        blockToVacate.vacate()
    }

    def blockIsOccupied(position: BlockPosition): Boolean = {
        val laneOfInterest = position.getLane()
        val blockNumberOfInterest = position.getBlockNumber()
        val blockOfInterest = blocks.find(x => x.getPosition().getLane() == laneOfInterest && x.getPosition().getBlockNumber() == blockNumberOfInterest)
        val hasPlayer = blockOfInterest.isDefined && (blockOfInterest.get.getOccupiedByPlayerWithId() != Config.EMPTY_PLAYER)
        return hasPlayer
    }

    def applyOilToBlock(position: BlockPosition) =
        getBlock(position).setMapObject(Config.OIL_SPILL_MAP_OBJECT)

    private def getBlock(position: BlockPosition): Block = {
        val laneOfInterest = position.getLane()
        val blockNumberOfInterest = position.getBlockNumber()
        val indexOfBlockOfInterest = blocks.indexWhere(x => x.getPosition().getLane() == laneOfInterest && x.getPosition().getBlockNumber() == blockNumberOfInterest)
        val blockOfInterest = blocks(indexOfBlockOfInterest)
        return blockOfInterest
    }

    def getBlocks(): Array[Block] = {
        return blocks
    }

    def makeAllBlocksEmpty() = {
        val newBlocks = blocks.map(x => {
            new Block(new BlockPosition(x.getPosition().getLane(), x.getPosition().getBlockNumber()), Config.EMPTY_MAP_OBJECT, x.occupiedByPlayerWithId)
        })
        blocks = newBlocks
    }

    def placeObjectAt(lane: Int, blockNumber: Int, mapObject: Int) = {
        blocks.find(x => x.getPosition().getLane() == lane && x.getPosition().getBlockNumber() == blockNumber).get.mapObject = mapObject
    }

    def stageFuturePosition(stagedPosition: StagedPosition) = {
        stagedFuturePositions = stagedFuturePositions.appended(stagedPosition)
    }

    def stageCyberTruckAt(stagedCyberTruckPosition: StagedPosition) = {
        stagedCyberTruckPositions = stagedCyberTruckPositions.appended(stagedCyberTruckPosition)
    }

    def resolveCyberTruckCollisions(): Boolean = {
        var playerHitCyberTruck = false
        stagedFuturePositions.foreach(x => {
            val startBlockNumber = x.getOldPosition().getBlockNumber()
            val endBlockNumber = x.getNewPosition().getBlockNumber()
            val endLane = x.getNewPosition().getLane()
            val blockWithCyberTruckInMiddleOfPath = blocks.find(b => (b.getPosition().getLane() == endLane
              && b.getPosition().getBlockNumber() >= startBlockNumber
              && b.getPosition().getBlockNumber() <= endBlockNumber-1)
              && b.isOccupiedByCyberTruck()
            )
            val lastBlockWithCyberTruck = blocks.find(b => b.getPosition().getLane() == endLane
              && b.getPosition().getBlockNumber() == endBlockNumber
              && b.isOccupiedByCyberTruck()
            )
            val middleOfPathHasCyberTruckAndPlayerIsOnTheGround = blockWithCyberTruckInMiddleOfPath.isDefined && !x.getPlayer().isLizarding
            val playerCollidesWithCyberTruck = middleOfPathHasCyberTruckAndPlayerIsOnTheGround || lastBlockWithCyberTruck.isDefined
            if(playerCollidesWithCyberTruck) {
                if(middleOfPathHasCyberTruckAndPlayerIsOnTheGround) {
                    val definedBlockWithCyberTruck = blockWithCyberTruckInMiddleOfPath.get
                    val adjustedNewLane = definedBlockWithCyberTruck.getPosition().getLane()
                    val adjustedNewBlockNumber = definedBlockWithCyberTruck.getPosition().getBlockNumber() - 1
                    val newFuturePosition = new BlockPosition(adjustedNewLane, adjustedNewBlockNumber)
                    x.setNewPosition(newFuturePosition)
                    x.getPlayer().hitCyberTruck()
                    definedBlockWithCyberTruck.removeCyberTruck()
                    playerHitCyberTruck = true
                }
                else {
                    val definedBlockWithCyberTruck = lastBlockWithCyberTruck.get
                    val adjustedNewLane = definedBlockWithCyberTruck.getPosition().getLane()
                    val adjustedNewBlockNumber = definedBlockWithCyberTruck.getPosition().getBlockNumber() - 1
                    val newFuturePosition = new BlockPosition(adjustedNewLane, adjustedNewBlockNumber)
                    x.setNewPosition(newFuturePosition)
                    x.getPlayer().hitCyberTruck()
                    definedBlockWithCyberTruck.removeCyberTruck()
                    playerHitCyberTruck = true
                }
            }
        })
        return playerHitCyberTruck
    }

    def resolvePlayerCollisions(): Boolean = {
        val player1StagedPosition = stagedFuturePositions.find(x => x.getPlayer().getGamePlayerId() == 1).get
        val player1FuturePosition = player1StagedPosition.getNewPosition()

        val player2StagedPosition = stagedFuturePositions.find(x => x.getPlayer().getGamePlayerId() == 2).get
        val player2FuturePosition = player2StagedPosition.getNewPosition()

        val playersFuturePositionsAreSame = (player1FuturePosition.getLane() == player2FuturePosition.getLane()) && (player1FuturePosition.getBlockNumber() == player2FuturePosition.getBlockNumber())
        val anyPlayerWasLizarding = player1StagedPosition.getPlayer().isLizarding || player2StagedPosition.getPlayer().isLizarding

        val player1WasInSameLaneAsPlayer2 = (player1StagedPosition.getOldPosition().getLane() == player2StagedPosition.getOldPosition().getLane())
        val player1WasBehindPlayer2 = (player1StagedPosition.getOldPosition().getBlockNumber() < player2StagedPosition.getOldPosition().getBlockNumber())
        val player1EndedUpInFrontOfPlayer2 = (player1FuturePosition.getBlockNumber() >= player2FuturePosition.getBlockNumber())
        val player1EndedUpInSameLaneAsPlayer2 = (player1FuturePosition.getLane() == player2FuturePosition.getLane())
        val player1DroveIntoPlayer2 = player1WasInSameLaneAsPlayer2 && player1WasBehindPlayer2 && player1EndedUpInFrontOfPlayer2 && player1EndedUpInSameLaneAsPlayer2 && (playersFuturePositionsAreSame || !player1StagedPosition.getPlayer().isLizarding)

        val player2WasInSameLaneAsPlayer1 = (player2StagedPosition.getOldPosition().getLane() == player1StagedPosition.getOldPosition().getLane())
        val player2WasBehindPlayer1 = (player2StagedPosition.getOldPosition().getBlockNumber() < player1StagedPosition.getOldPosition().getBlockNumber())
        val player2EndedUpInFrontOfPlayer1 = (player2FuturePosition.getBlockNumber() >= player1FuturePosition.getBlockNumber())
        val player2EndedUpInSameLaneAsPlayer1 = (player2FuturePosition.getLane() == player1FuturePosition.getLane())
        val player2DroveIntoPlayer1 = player2WasInSameLaneAsPlayer1 && player2WasBehindPlayer1 && player2EndedUpInFrontOfPlayer1 && player2EndedUpInSameLaneAsPlayer1 && (playersFuturePositionsAreSame || !player2StagedPosition.getPlayer().isLizarding)

        val isCollisionFromBehind = (player1DroveIntoPlayer2 || player2DroveIntoPlayer1) && !anyPlayerWasLizarding;

        val isCollision = playersFuturePositionsAreSame || isCollisionFromBehind

        if (!isCollision) {
            return false
        }

        if (isCollisionFromBehind) {
            val stagedPositionOfPlayerInFront = if (player1DroveIntoPlayer2) player2StagedPosition
            else player1StagedPosition

            val stagedPositionOfPlayerCollidingFromBehind = stagedFuturePositions.find(x => x != stagedPositionOfPlayerInFront).get
            val correctedBlockNumber = stagedPositionOfPlayerInFront.getNewPosition().getBlockNumber() - 1
            val correctedLane = stagedPositionOfPlayerCollidingFromBehind.getNewPosition().getLane()
            val correctedPositionOfPlayerCollidingFromBehind = new BlockPosition(correctedLane, correctedBlockNumber)
            stagedPositionOfPlayerCollidingFromBehind.setNewPosition(correctedPositionOfPlayerCollidingFromBehind)

            return true
        }

        val collisionFromTheSide = playersFuturePositionsAreSame
        if (collisionFromTheSide) {
            val correctedPlayer1Lane = player1StagedPosition.getOldPosition().getLane()
            val correctedPlayer1BlockNumber = player1FuturePosition.getBlockNumber() - 1
            val correctedPlayer1FuturePosition = new BlockPosition(correctedPlayer1Lane, correctedPlayer1BlockNumber)
            player1StagedPosition.setNewPosition(correctedPlayer1FuturePosition)

            val correctedPlayer2Lane = player2StagedPosition.getOldPosition().getLane()
            val correctedPlayer2BlockNumber = player2FuturePosition.getBlockNumber() - 1
            val correctedPlayer2FuturePosition = new BlockPosition(correctedPlayer2Lane, correctedPlayer2BlockNumber)
            player2StagedPosition.setNewPosition(correctedPlayer2FuturePosition)
            return true
        }

        throw new Exception("A collision occurred that was not from the side or from behind. This should not be possible since cars cannot travel in reverse")
    }

    def placeRequestedCyberTrucks(): Boolean =
    {
        var thereWasACollision = false

        val haveTweetsFromBothPlayers = stagedCyberTruckPositions.length == 2
        if(haveTweetsFromBothPlayers)
        {
            val position1 = stagedCyberTruckPositions(0).getNewPosition()
            val position2 = stagedCyberTruckPositions(1).getNewPosition()
            val bothPlayersTryingToPlaceCyberTrucksAtSameLocation = position1.getLane() == position2.getLane() && position1.getBlockNumber() == position2.getBlockNumber()
            if(bothPlayersTryingToPlaceCyberTrucksAtSameLocation) {
                stagedCyberTruckPositions(0).getPlayer().refundTweet()
                stagedCyberTruckPositions(1).getPlayer().refundTweet()
                thereWasACollision = true
                return thereWasACollision
            }
        }

        stagedCyberTruckPositions.foreach(x => {
            val blockForNewCyberTruck = getBlockMatchingPosition(x.getNewPosition())
           val blockAlreadyContainsCyberTruck = blockForNewCyberTruck.isOccupiedByCyberTruck()
           if(blockAlreadyContainsCyberTruck) {
               x.getPlayer().refundTweet()
               thereWasACollision = true
           } else {
               val thereIsAnOldCyberTruck = x.getOldPosition() != null
               if(thereIsAnOldCyberTruck) {
                   val blockForOldCyberTruck = getBlockMatchingPosition(x.getOldPosition())
                   blockForOldCyberTruck.removeCyberTruck()
               }
               blockForNewCyberTruck.addCyberTruck()
               x.getPlayer().setCurrentCyberTruckPosition(blockForNewCyberTruck.getPosition())
           }
        })

        stagedCyberTruckPositions = List[StagedPosition]()
        return thereWasACollision
    }

    def calculateEffectsOfAndApplyStagedPositionsToPlayers() = {
        stagedFuturePositions.foreach(x => {
            val player: CarGamePlayer = x.getPlayer()
            val newPosition = x.getNewPosition()
            val oldPosition = x.getOldPosition()

            val positionToStartCounting = findPositionToStartCountingCollisionsFrom(oldPosition, player.hasTurnedThisRound(), newPosition)
            val positionToEndCounting = findPositionToEndCountingCollisionsFromInMiddlePath(newPosition)
            val lastBlockPosition = findLastPositionForIdentifyingEffectsAtEndOfPath(newPosition)

            applyMapEffectsInMiddlePathToPlayer(player, positionToStartCounting, positionToEndCounting)
            applyMapEffectsInLastBlockOfPathToPlayer(player, lastBlockPosition)
            applyPickupsInPathToPlayer(player, positionToStartCounting, positionToEndCounting)
            applyPickupsInLastBlockToPlayer(player, lastBlockPosition)
            occupyFinalMapPositionForPlayer(player.getGamePlayerId(), newPosition)
            checkIfPlayerHasWon(player, newPosition)
        })
        stagedFuturePositions = List[StagedPosition]()
    }

    private def findPositionToStartCountingCollisionsFrom(oldPosition: BlockPosition, hasTurned: Boolean, newPosition: BlockPosition): BlockPosition = {
        if (hasTurned) {
            // If they turned, we need to count from the new lane but the same x position from the old position
            return new BlockPosition(newPosition.getLane(), oldPosition.getBlockNumber())
        } else {
            // If they did not turn, apply the start counter from the very next x position to prevent duplicate application of effects
            return new BlockPosition(oldPosition.getLane(), oldPosition.getBlockNumber() + 1)
        }
    }

    private def findPositionToEndCountingCollisionsFromInMiddlePath(newPosition: BlockPosition): BlockPosition = {
        // End counting all map effects one block early, so that the last block can be handled separately
        return new BlockPosition(newPosition.getLane(), newPosition.getBlockNumber() - 1)
    }

    private def findLastPositionForIdentifyingEffectsAtEndOfPath(newPosition: BlockPosition): BlockPosition = {
        return new BlockPosition(newPosition.getLane(), newPosition.getBlockNumber())
    }

    def applyMapEffectsInMiddlePathToPlayer(carGamePlayer: CarGamePlayer, positionToStartCounting: BlockPosition, positionToEndCounting: BlockPosition) = {
        val playerHitMudCount = mudCountInPath(positionToStartCounting, positionToEndCounting)
        for (a <- 0 until playerHitMudCount) {
            carGamePlayer.hitItem(Config.MUD_MAP_OBJECT)
        }

        val playerHitOilCount = oilSpillCountInPath(positionToStartCounting, positionToEndCounting)
        for (a <- 0 until playerHitOilCount) {
            carGamePlayer.hitItem(Config.OIL_SPILL_MAP_OBJECT)
        }

        val playerHitWallCount = wallCountInPath(positionToStartCounting, positionToEndCounting)
        for (a <- 0 until playerHitWallCount) {
            carGamePlayer.hitItem(Config.WALL_MAP_OBJECT)
        }
    }

    def mudCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
        numberOfMapObjectsInPath(startPosition, endPosition, Config.MUD_MAP_OBJECT)

    def oilSpillCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
        numberOfMapObjectsInPath(startPosition, endPosition, Config.OIL_SPILL_MAP_OBJECT)

    private def numberOfMapObjectsInPath(startPosition: BlockPosition, endPosition: BlockPosition, mapObject: Int): Int = {
        val startLane = startPosition.getLane()
        val startBlockNumber = startPosition.getBlockNumber()
        val endLane = endPosition.getLane()
        val endBlockNumber = endPosition.getBlockNumber()
        val blocksWithObject = blocks.count(b => (b.getPosition().getLane() == endLane
            && b.getPosition().getBlockNumber() >= startBlockNumber
            && b.getPosition().getBlockNumber() <= endBlockNumber)
            && b.getMapObject() == mapObject
        )
        return blocksWithObject
    }

    def wallCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
        numberOfMapObjectsInPath(startPosition, endPosition, Config.WALL_MAP_OBJECT)

    def getBlockMatchingPosition(blockPosition: BlockPosition): Block = {
        val blockIndex = blocks.indexWhere(block => block.getPosition().getBlockNumber() == blockPosition.getBlockNumber() && block.getPosition().getLane() == blockPosition.getLane())
        return blocks(blockIndex)
    }

    def applyMapEffectsInLastBlockOfPathToPlayer(carGamePlayer: CarGamePlayer, lastBlockPosition: BlockPosition) = {
        val blockToApply = getBlockMatchingPosition(lastBlockPosition)
        val wasLizarding = carGamePlayer.isLizarding
        carGamePlayer.setLizarding(false)
        carGamePlayer.hitItem(blockToApply.getMapObject())
        carGamePlayer.setLizarding(wasLizarding)
    }

    def applyPickupsInPathToPlayer(carGamePlayer: CarGamePlayer, positionToStartCounting: BlockPosition, positionToEndCounting: BlockPosition) = {
        val playerPickedUpOilItemCount = oilItemCountInPath(positionToStartCounting, positionToEndCounting)
        for (a <- 0 until playerPickedUpOilItemCount) {
            carGamePlayer.pickupItem(Config.OIL_ITEM_MAP_OBJECT)
        }

        val playerPickedUpBoostCount = boostCountInPath(positionToStartCounting, positionToEndCounting)
        for (a <- 0 until playerPickedUpBoostCount) {
            carGamePlayer.pickupItem(Config.BOOST_MAP_OBJECT)
        }

        val playerPickedUpLizardCount = lizardCountInPath(positionToStartCounting, positionToEndCounting)
        for (a <- 0 until playerPickedUpLizardCount) {
            carGamePlayer.pickupItem(Config.LIZARD_MAP_OBJECT)
        }

        val playerPickedUpTweetCount = tweetCountInPath(positionToStartCounting, positionToEndCounting)
        for (a <- 0 until playerPickedUpTweetCount) {
            carGamePlayer.pickupItem(Config.TWEET_MAP_OBJECT)
        }
    }

    def oilItemCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
        numberOfMapObjectsInPath(startPosition, endPosition, Config.OIL_ITEM_MAP_OBJECT)

    def boostCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
        numberOfMapObjectsInPath(startPosition, endPosition, Config.BOOST_MAP_OBJECT)

    def lizardCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
        numberOfMapObjectsInPath(startPosition, endPosition, Config.LIZARD_MAP_OBJECT)

    def tweetCountInPath(startPosition: BlockPosition, endPosition: BlockPosition): Int =
    numberOfMapObjectsInPath(startPosition, endPosition, Config.TWEET_MAP_OBJECT)

    def applyPickupsInLastBlockToPlayer(carGamePlayer: CarGamePlayer, lastBlockPosition: BlockPosition) = {
        val blockToApply = getBlockMatchingPosition(lastBlockPosition)
        val wasLizarding = carGamePlayer.isLizarding
        carGamePlayer.setLizarding(false)
        carGamePlayer.pickupItem(blockToApply.getMapObject())
        carGamePlayer.setLizarding(wasLizarding)

    }

    private def checkIfPlayerHasWon(carGamePlayer: CarGamePlayer, newPosition: BlockPosition) = {
        if (newPosition.getBlockNumber() == Config.TRACK_LENGTH) {
            carGamePlayer.finish()
        }
    }

    def occupyFinalMapPositionForPlayer(gamePlayerId: Int, position: BlockPosition) = {
        val blockToOccupy = getBlock(position)
        blockToOccupy.occupy(gamePlayerId)
    }

    private def pathIncludesMapObject(startPosition: BlockPosition, endPosition: BlockPosition, mapObject: Int): Boolean = {
        val startLane = startPosition.getLane()
        val startBlockNumber = startPosition.getBlockNumber()
        val endLane = endPosition.getLane()
        val endBlockNumber = endPosition.getBlockNumber()
        val blocksWithObject =
            blocks.find(x =>
                (x.getPosition().getLane() == endLane && x.getPosition().getBlockNumber() >= startBlockNumber && x.getPosition().getBlockNumber() <= endBlockNumber)
                    &&
                    x.getMapObject() == mapObject
            )
        val pathIncludesObject = blocksWithObject.isDefined
        return pathIncludesObject
    }
}
