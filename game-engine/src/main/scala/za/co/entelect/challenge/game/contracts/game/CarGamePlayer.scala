package za.co.entelect.challenge.game.contracts.game

import za.co.entelect.challenge.game.contracts.Config.Config
import za.co.entelect.challenge.game.contracts.map.BlockPosition

import scala.collection.mutable

class CarGamePlayer(health: Int, var score: Int, gamePlayerId: Int, var speed: Int) extends GamePlayer {

    private val MINIMUM_SPEED: Int = Config.MINIMUM_SPEED;
    private val SPEED_STATE_1: Int = Config.SPEED_STATE_1;
    private val INITIAL_SPEED: Int = Config.INITIAL_SPEED;
    private val SPEED_STATE_2: Int = Config.SPEED_STATE_2;
    private val SPEED_STATE_3: Int = Config.SPEED_STATE_3;
    private val MAXIMUM_SPEED: Int = Config.MAXIMUM_SPEED;
    private val BOOST_SPEED: Int = Config.BOOST_SPEED;
    private val powerups: mutable.ListBuffer[String] = mutable.ListBuffer[String]();
    private var state: String = "";
    private var boosting: Boolean = false;
    private var boostCounter = 0;
    private var lizarding: Boolean = false;
    private var currentCyberTruckPosition: BlockPosition = null;

    override def getHealth: Int = {
        return health;
    }

    override def getScore: Int = {
        return score;
    }

    def getGamePlayerId(): Int = {
        return gamePlayerId;
    }

    def getSpeed(): Int = {
        return speed;
    }

    def getState(): String = {
        return state;
    }

    def getReady() = {
        setState(Config.READY_PLAYER_STATE);
    }

    def doNothing() = {
        setState(Config.NOTHING_PLAYER_STATE);
    }

    def turnRight() = {
        setState(Config.TURNING_RIGHT_PLAYER_STATE);
    }

    def turnLeft() = {
        setState(Config.TURNING_LEFT_PLAYER_STATE);
    }

    def hasTurnedThisRound(): Boolean = {
        return state == Config.TURNING_LEFT_PLAYER_STATE || state == Config.TURNING_RIGHT_PLAYER_STATE;
    }

    def hitItem(item: Int): Unit = {
        if (isLizarding) return;
        if (item == Config.MUD_MAP_OBJECT) {
            hitMud()
        }
        if (item == Config.OIL_SPILL_MAP_OBJECT) {
            hitOil()
        }
        if (item == Config.WALL_MAP_OBJECT) {
            hitWall()
        }
    }

    def hitMud(): Unit = {
        val allowStop: Boolean = false;
        reduceSpeed(allowStop);
        setState(Config.HIT_MUD_PLAYER_STATE);
        updateScore(Config.HIT_MUD_SCORE_PENALTY);
    }

    def hitOil(): Unit = {
        val allowStop: Boolean = false;
        reduceSpeed(allowStop);
        setState(Config.HIT_OIL_PLAYER_STATE);
        updateScore(Config.HIT_OIL_SCORE_PENALTY);
    }

    def hitWall(): Unit = {
        val allowStop: Boolean = false
        reduceSpeedToLevel(allowStop, SPEED_STATE_1);
        setState(Config.HIT_WALL_PLAYER_STATE)
        updateScore(Config.HIT_WALL_SCORE_PENALTY)
    }

    private def reduceSpeedToLevel(allowStop: Boolean, speedLevel: Int) = {
        //any form of deceleration cancels the boost (player command/ obstacle)
        if (boosting) {
            boosting = false;
            boostCounter = 0;
        }

        speed = speedLevel;
    }

    def hasOilItem: Boolean = powerups.contains(Config.OIL_POWERUP_ITEM)

    def useOilItem(): Unit = {
        powerups.subtractOne(Config.OIL_POWERUP_ITEM)
        setState(Config.USED_POWERUP_OIL_PLAYER_STATE)
        updateScore(Config.USE_POWERUP_BONUS)
    }

    def useLizard(): Unit = {
        powerups.subtractOne(Config.LIZARD_POWERUP_ITEM)
        setState(Config.USED_POWERUP_LIZARD_PLAYER_STATE)
        this.lizarding = true;
    }

    def pickupItem(pickupItem: Int): Unit = {
        if (isLizarding) return;
        if (pickupItem == Config.BOOST_MAP_OBJECT) {
            pickupBoost()
        }
        if (pickupItem == Config.OIL_ITEM_MAP_OBJECT) {
            pickupOilItem()
        }
        if (pickupItem == Config.LIZARD_MAP_OBJECT) {
            pickupLizard()
        }
        if(pickupItem == Config.TWEET_MAP_OBJECT)
        {
            pickupTweet();
        }
    }

    def pickupOilItem(isLastBlock: Boolean = false): Unit = {
        powerups.addOne(Config.OIL_POWERUP_ITEM);
        updateScore(Config.PICKUP_POWERUP_BONUS);
    }

    def pickupBoost() = {
        powerups.addOne(Config.BOOST_POWERUP_ITEM);
        updateScore(Config.PICKUP_POWERUP_BONUS);
    }

    def pickupLizard() = {
        powerups.addOne(Config.LIZARD_POWERUP_ITEM);
        updateScore(Config.PICKUP_POWERUP_BONUS);
    }

    def pickupTweet() = {
        powerups.addOne(Config.TWEET_POWERUP_ITEM)
        updateScore(Config.PICKUP_POWERUP_BONUS)
    }

    def isLizarding: Boolean = {
        return lizarding;
    }

    def setLizarding(lizarding: Boolean) = {
        this.lizarding = lizarding
    }

    def hasBoost(): Boolean = {
        val playerHasBoost = powerups.contains(Config.BOOST_POWERUP_ITEM);
        return playerHasBoost;
    }

    def hasTweet(): Boolean = {
        val playerHasTweet = powerups.contains(Config.TWEET_POWERUP_ITEM);
        return playerHasTweet
    }

    def useBoost() = {
        powerups.subtractOne(Config.BOOST_POWERUP_ITEM);
        speed = BOOST_SPEED;
        boosting = true;
        boostCounter = Config.BOOST_DURATION;
        setState(Config.USED_POWERUP_BOOST_PLAYER_STATE);
        updateScore(Config.USE_POWERUP_BONUS);
    }

    def useTweet() = {
        powerups.subtractOne(Config.TWEET_POWERUP_ITEM);
        setState(Config.USED_POWERUP_TWEET_PLAYER_STATE);
        updateScore(Config.USE_POWERUP_BONUS);
    }

    private def updateScore(scoreChange: Int) = {
        score += scoreChange;
    }

    def isBoosting(): Boolean = {
        return boosting;
    }

    def tickBoost() = {
        boostCounter -= 1;
        val boostOver = boostCounter == 0;
        if (boostOver) {
            boosting = false;
            speed = MAXIMUM_SPEED;
            setState(Config.NOTHING_PLAYER_STATE);
        }
    }

    def decelerate() = {
        val allowStop = true;
        reduceSpeed(allowStop);
        setState(Config.DECELERATING_PLAYER_STATE);
    }

    private def reduceSpeed(allowStop: Boolean) = {
        //any form of deceleration cancels the boost (player command/ obstacle)
        if (boosting) {
            boosting = false;
            boostCounter = 0;
        }

        speed match {
            case MINIMUM_SPEED => speed = MINIMUM_SPEED
            case SPEED_STATE_1 => speed = if (allowStop) MINIMUM_SPEED else SPEED_STATE_1
            case INITIAL_SPEED => speed = SPEED_STATE_1
            case SPEED_STATE_2 => speed = SPEED_STATE_1
            case SPEED_STATE_3 => speed = SPEED_STATE_2
            case MAXIMUM_SPEED => speed = SPEED_STATE_3
            case BOOST_SPEED => speed = MAXIMUM_SPEED
            case invalidSpeed => throw new Exception("Invalid current speed: " + invalidSpeed.toString())
        };
    }

    def accelerate() = {
        increaseSpeed();
        setState(Config.ACCELERATING_PLAYER_STATE);
    }

    private def increaseSpeed() = {
        speed match {
            case MINIMUM_SPEED => speed = SPEED_STATE_1
            case SPEED_STATE_1 => speed = SPEED_STATE_2
            case INITIAL_SPEED => speed = SPEED_STATE_2
            case SPEED_STATE_2 => speed = SPEED_STATE_3
            case SPEED_STATE_3 => speed = MAXIMUM_SPEED
            case MAXIMUM_SPEED => speed = MAXIMUM_SPEED
            case BOOST_SPEED => speed = BOOST_SPEED
            case invalidSpeed => throw new Exception("Invalid current speed: " + invalidSpeed.toString())
        };
    }

    def finish() = {
        setState(Config.FINISHED_PLAYER_STATE);
    }

    private def setState(newPlayerState: String) = {
        state = newPlayerState;
    }

    def getPowerups(): Array[String] = {
        return powerups.toArray
    }

    def getBoostCounter(): Int = {
        return boostCounter;
    }

    def hasLizard: Boolean = {
        val playerHasBoost = powerups.contains(Config.LIZARD_POWERUP_ITEM);
        return playerHasBoost;
    }

    def getCurrentCyberTruckPosition(): BlockPosition = {
        return currentCyberTruckPosition;
    }

    def setCurrentCyberTruckPosition(newCyberTruckPosition: BlockPosition) = {
        currentCyberTruckPosition = newCyberTruckPosition
    }

    def refundTweet() = {
        powerups.addOne(Config.TWEET_POWERUP_ITEM)
        updateScore(-1 * Config.USE_POWERUP_BONUS)
    }

    def hitCyberTruck() = {
        setState(Config.HIT_CYBER_TRUCK_PLAYER_STATE);
        speed = Config.SPEED_STATE_1
        updateScore(Config.HIT_CYBERTRUCK_SCORE_PENALTY)
    }

}
