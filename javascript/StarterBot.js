'use strict';

let fs = require('fs');
let readline = require('readline');
let stateFileName = 'state.json';

let stateFile;
let myPlayer;
let opponent;
let mapSize;
let tiles;

function executeRound(roundNumber) {
    // Read the current state and choose an action
    stateFile = fs.readFileSync(`./rounds/${roundNumber}/${stateFileName}`);
    stateFile = JSON.parse(stateFile);

    setupEntities();

    let command = runStrategy();
    console.log(`C;${roundNumber};${command}`);
}

function setupEntities() {
    myPlayer = stateFile.player;
    opponent = stateFile.opponent;
    mapSize = stateFile.worldMap.length;
    tiles = flatMap(stateFile.worldMap);
}

function runStrategy() {
    // Check if there is mud on the road ahead
    let isMudAhead = false;
    for (const tile of tiles) {
        // Only check blocks from player position up to a few blocks forward
        const isSomeBlockAhead = (tile.position.x > myPlayer.position.x)
            && (tile.position.x < myPlayer.position.x + myPlayer.speed);
        const isSameLane = (tile.position.y === myPlayer.position.y);
        const isMudBlock = (tile.surfaceObject === mapObjectNames.MUD_MAP_OBJECT);
        // Mark isMudAhead as true when mud has been detected once
        isMudAhead = isMudAhead || (isSomeBlockAhead && isSameLane && isMudBlock);
    }
    if (isMudAhead) {
        if (Math.random() > .5) {
            return commandNames.TURN_LEFT;
        } else {
            return commandNames.TURN_RIGHT;
        }
    }

    // isMudAhead was false, so lets keep the speed up
    if (myPlayer.speed < 9) {
        return commandNames.ACCELERATE;
    }

    // Always return a command, even if we have nothing to do (because non-responsive bots get their score penalized)
    return commandNames.NOTHING;
}

// Enums allow you to use less magic-strings
let commandNames = {
    NOTHING: 'NOTHING',
    TURN_LEFT: 'TURN_LEFT',
    TURN_RIGHT: 'TURN_RIGHT',
    ACCELERATE: 'ACCELERATE',
    DECELERATE: 'DECELERATE',
    USE_BOOST: 'USE_BOOST',
    USE_OIL: 'USE_OIL'
};

let powerupNames = {
    OIL: 'OIL',
    BOOST: 'BOOST',
};

let mapObjectNames = {
    EMPTY_MAP_OBJECT: 0,
    MUD_MAP_OBJECT: 1,
    OIL_SPILL_MAP_OBJECT: 2,
    OIL_ITEM_MAP_OBJECT: 3,
    FINISH_LINE_MAP_OBJECT: 4,
    BOOST_MAP_OBJECT: 5,
    STARTING_BLOCK_FOR_GENERATED_MAP_OBJECTS: 6,
};

/***
 * Returns an array with one less level of nesting
 * @param array
 * @returns {Array}
 */
function flatMap(array) {
    return array.reduce((acc, x) => acc.concat(x), []);
}

/***
 * Runs the bot using standard in/out
 * The starter-pack will use this to initiate new rounds,
 * and in turn get a new command from the bot
 */
let consoleReader = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});
consoleReader.on('line', roundNumber => {
    executeRound(roundNumber); // Read in the current round number
});

// executeRound(1);
