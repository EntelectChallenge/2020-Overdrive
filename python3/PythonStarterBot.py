# -*- coding: utf-8 -*-
"""
Entelect StarterBot for Python3
"""
import json
import os
import logging
import numpy as np
import random

from Lane import Lane
from BlockObject import BlockObject
from Player import Player
from Position import Position
from State import State
from Commands import Commands

logging.basicConfig(filename='sample_python_bot.log', filemode='w', level=logging.DEBUG)
logger = logging.getLogger(__name__)


class StarterBot:

    def __init__(self):
        """
        Initialize Bot .
        """
        self.max_speed = 9
        self.random_list = [-1,1]

        self.current_round = None
        self.max_rounds = None
        self.command = ''
        self.map = None  # Array of lanes
        self.player_info = None  # Player object
        self.game_state = None
        self.raw_lanes = []
        self.raw_player = None


    def get_current_round_details(self):
        """
        Reads in all relevant information required for the tick.
        """

        state_location = os.path.join('rounds', str(self.current_round), 'state.json')
        self.game_state = self.load_state_json(state_location)

        self.command = ''

        self.raw_lanes = self.game_state['lanes']
        self.map = self.get_lanes()

        self.raw_player = self.game_state['player']
        self.player_info = self.get_player()

        self.current_round = self.game_state['currentRound']
        self.max_rounds = self.game_state['maxRounds']

        return None

    def get_player(self):
        player = Player

        player.id = self.raw_player['id']
        player.speed = self.raw_player['speed']
        player.state = State[self.raw_player['state']]
        player.position = Position(
                        self.raw_player['position']['lane'],
                        self.raw_player['position']['blockNumber'])
        player.power_ups = self.raw_player['powerups']
        player.boosting = self.raw_player['boosting']
        player.boost_counter = self.raw_player['boost-counter']

        return player


    def get_lanes(self):
        lanes = []
        for lane in self.raw_lanes:
            raw_position = lane['position']
            lanes.append(
                Lane(
                    Position(
                        raw_position['lane'],
                        raw_position['blockNumber']
                    ),
                    BlockObject(lane['object']),
                    lane['occupiedByPlayerWithId']
                )
            )

        return lanes
    
    def getListMapStructure(self):
        map = {}

        totalLanes = len(self.map)
        lastLane = self.map[totalLanes - 1]

        mapHeight = lastLane.position.lane
        mapWidth = int(totalLanes/mapHeight)

        for lane in range(1, mapHeight+1):
            blocks = []
            for block in range(0, mapWidth):
                blocks.append(self.map[((lane - 1) * mapWidth) + block])

            map[lane] = blocks

        return map
    
    def getNextBlocks(self, lane, block, maxSpeed):
        map = self.getListMapStructure()
        blockTypes = []

        startBlock = self.map[0].position.block

        laneList = map[lane]

        for block in range(block - startBlock, np.minimum(block - startBlock + maxSpeed, len(laneList))):
            if (laneList[block] == None):
                break
            
            blockTypes.append(laneList[block].object)

        return blockTypes
    
    def changeLaneCommand(self, laneIndicator):
        direction = "LEFT"

        if (laneIndicator == 1):
            direction ="RIGHT"

        return direction


    def starter_bot_logic(self):
        
        nextBlocks = self.getNextBlocks(self.player_info.position.lane, self.player_info.position.block, 9)

        if BlockObject.MUD in nextBlocks:
            self.command = Commands.TURN.value + self.changeLaneCommand(random.choice(self.random_list))
        else:
            self.command = Commands.ACCELERATE

        return Commands.NOTHING

    def write_action(self):
        """
        command in form : C;<round number>;<command>
        """

        print(f'C;{self.current_round};{self.command}')
        logger.info(f'Writing command : C;{self.current_round};{self.command};')

        return None

    def load_state_json(self, state_location):
        """
        Gets the current Game State json file.
        """
        json_map = ''
        try:
            json_map = json.load(open(state_location, 'rt'))
        except IOError:
            logger.error("Cannot load Game State")
        return json_map

    def wait_for_round_start(self):
        next_round = int(input())
        return next_round

    def run_bot(self):
        logger.info("Bot has started Running")
        while True:
            logger.info('Waiting for next round.')
            next_round_number = self.wait_for_round_start()
            logger.info('Starting Round : ' + str(next_round_number))
            self.current_round = next_round_number
            self.get_current_round_details()
            logger.info('Beginning StarterBot Logic Sequence')
            self.starter_bot_logic()

            self.write_action()


if __name__ == '__main__':
    bot = StarterBot()
    bot.run_bot()
