# -*- coding: utf-8 -*-
"""
Entelect StarterBot for Python3
"""
import json
import os
import logging
import numpy as np
from scipy.spatial import distance

from Lane import Lane
from BlockObject import BlockObject
from Player import Player
from DirectionChange import DirectionChange
from Position import Position
from State import State

logging.basicConfig(filename='sample_python_bot.log', filemode='w', level=logging.DEBUG)
logger = logging.getLogger(__name__)


class StarterBot:

    def __init__(self):
        """
        Initialize Bot .
        """

        self.current_round = None
        self.max_rounds = None
        self.command = ''
        self.map = None  # Array of lanes
        self.player_info = None  # Player object
        self.game_state = None
        self.raw_lanes = None
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
        # self.max_rounds = self.game_state['maxRounds']

        return None

    def get_player(self):
        player = Player

        player.id = self.raw_player['id']
        player.speed = self.raw_player['speed']
        player.state = self.get_player_state()
        player.position = Position(
                        self.raw_player['lane'],
                        self.raw_player['blockNumber'])
        return player

    def get_player_state(self, state):
        if state == 'ready':
            return State.ready
        else:
            return State.accelerating

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
                    self.get_object(lane['object']),
                    lane['occupied-by-player-with-id']
                )
            )

        return lanes

    def get_object(self, raw_object):
        if raw_object == 'MUD':
            return BlockObject.Mud
        else:
            return BlockObject.Nothing

    def starter_bot_logic(self):
        """
        Do nothing .....


        ****THIS IS WHERE YOU CAN ADD OR CHANGE THE LOGIC OF THE BOT****
        """

        self.command = 'No Command'

        return None

    def write_action(self):
        """
        command in form : C;<round number>;<command>
        """

        print(f'C;{self.current_tick};{self.command}')
        logger.info(f'Writing command : C;{self.current_tick};{self.command};')

        return None

    def load_state_json(self, state_location):
        """
        Gets the current Game State json file.
        """
        json_map = ''
        try:
            json_map = json.load(open(state_location, 'r'))
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
