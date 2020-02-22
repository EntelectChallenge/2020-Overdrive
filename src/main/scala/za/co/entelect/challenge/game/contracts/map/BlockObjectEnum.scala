package za.co.entelect.challenge.game.contracts.map

class BlockObjectEnum() {

    def empty(): Int = {
        return 0;
    }

    def mud(): Int = {
        return 1;
    }

    def finishLine(): Int = {
        return 2;
    }

    def generateMapObject(randomNumberGenerator: scala.util.Random): Int = {
        val randomInt = randomNumberGenerator.nextInt(100);
        
        val tenPercentChanceOfMud = randomInt < 10;
        if(tenPercentChanceOfMud) {
            return mud();
        }
        else {
            return empty();
        }
    }
}