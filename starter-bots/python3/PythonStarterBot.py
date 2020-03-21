# -*- coding: utf-8 -*-
"""
Entelect StarterBot for Python3
"""
import json
import os
import logging
import random
import numpy as np

from Lane import Lane
from Player import Player
from Position import Position
from Enums import BlockObject, State, Commands, Direction

logging.basicConfig(filename='sample_python_bot.log', filemode='w', level=logging.DEBUG)
logger = logging.getLogger(__name__)


class StarterBot:

    def __init__(self):
        """
        Initialize Bot .
        """
        self.max_speed = 9
        self.random_list = [-1, 1]

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

        self.raw_lanes = self.game_state['worldMap']
        self.map = self.get_lanes()

        self.raw_player = self.game_state['player']
        self.player_info = self.get_player()

        self.current_round = self.game_state['currentRound']
        self.max_rounds = self.game_state['maxRounds']

        return None

    def get_player(self):
        player = Player(pid=self.raw_player['id'],
                        speed=self.raw_player['speed'],
                        state=State[self.raw_player['state']],
                        position=Position(self.raw_player['position']['y'],
                                          self.raw_player['position']['x']),
                        power_ups=self.raw_player['powerups'],
                        boosting=self.raw_player['boosting'],
                        boost_counter=self.raw_player['boostCounter'])
        return player

    def get_lanes(self):
        lanes = []
        for lane in self.raw_lanes:
            for cell in lane:
                raw_position = cell['position']
                lanes.append(Lane(Position(raw_position['y'],
                                           raw_position['x']),
                                  BlockObject(cell['surfaceObject']),
                                  cell['occupiedByPlayerId']))
        return lanes
    
    def get_list_map_structure(self):
        game_map = {}
        total_lanes = len(self.map)
        last_lane = self.map[total_lanes - 1]

        map_height = last_lane.position.lane
        map_width = int(total_lanes/map_height)

        for lane in range(1, map_height+1):
            blocks = []
            for block in range(0, map_width):
                blocks.append(self.map[((lane - 1) * map_width) + block])

            game_map[lane] = blocks

        return game_map
    
    def get_next_blocks(self, lane, block, max_speed):
        game_map = self.get_list_map_structure()
        block_types = []
        start_block = self.map[0].position.block
        lane_list = game_map[lane]

        for block in range(block - start_block, np.minimum(block - start_block + max_speed, len(lane_list))):
            if lane_list[block] is None:
                break
            block_types.append(lane_list[block].object)

        return block_types

    @staticmethod
    def change_lane_command(lane_indicator):
        direction = Direction.LEFT.value
        if lane_indicator == 1:
            direction = Direction.RIGHT.value
        return direction

    def starter_bot_logic(self):
        """
        Currently implemented logic :
        If there is a mud block in front of you, turn. Otherwise, accelerate.
        """
        next_blocks = self.get_next_blocks(self.player_info.position.lane, self.player_info.position.block, self.max_speed)

        if BlockObject.MUD in next_blocks:
            self.command = Commands.TURN.value + self.change_lane_command(random.choice(self.random_list))
        else:
            self.command = Commands.ACCELERATE.value

        return self.command

    def write_action(self):
        """
        command in form : C;<round number>;<command>
        """
        print(f'C;{self.current_round};{self.command}')
        logger.info(f'Writing command : C;{self.current_round};{self.command};')

        return None

    @staticmethod
    def load_state_json(state_location):
        """
        Gets the current Game State json file.
        """
        json_map = {}
        try:
            json_map = json.load(open(state_location, 'rt'))
        except IOError:
            logger.error("Cannot load Game State")
        return json_map

    @staticmethod
    def wait_for_round_start():
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
