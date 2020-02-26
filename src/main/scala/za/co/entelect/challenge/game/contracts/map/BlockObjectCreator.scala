package za.co.entelect.challenge.game.contracts.map

import za.co.entelect.challenge.game.contracts.Config.Config

class BlockObjectCreator() {

    def generateMapObject(randomNumberGenerator: scala.util.Random): Int = {
        val randomInt = randomNumberGenerator.nextInt(100);
        
        val isMud = randomInt < Config.MUD_GENERATION_PERCENTAGE;
        val isBoost = randomInt < Config.BOOST_GENERATION_PERCENTAGE + Config.MUD_GENERATION_PERCENTAGE;
        if(isMud) 
        {
            return Config.MUD_MAP_OBJECT;
        }
        else if (isBoost) 
        {
            return Config.BOOST_MAP_OBJECT;
        }
        else 
        {
            return Config.EMPTY_MAP_OBJECT;
        }
    }
}