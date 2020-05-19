package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math/rand"
	"os"
	"path"
	"strconv"
	"strings"
	"time"
)

func main() {
	rand.Seed(time.Now().Unix())

	reader := bufio.NewReader(os.Stdin)
	for true {
		roundNum, err := reader.ReadString('\n')
		if err != nil {
			fmt.Printf("Error reading from STDIN: %s\n", err)
			os.Exit(1)
		}

		roundNumber, err := strconv.Atoi(strings.Trim(roundNum, "\r\n"))
		if err != nil {
			fmt.Printf("Cannot parse input as integer: %s\n", err)
			os.Exit(1)
		}
		currentState := loadState(roundNumber)

		action := botLogic(currentState)
		fmt.Printf("C;%d;%s\r\n", roundNumber, action)
	}
}

func loadState(roundNumber int) *StateInput {
	stateLocation := path.Join("./rounds", strconv.Itoa(roundNumber), "state.json")
	inputText, err := ioutil.ReadFile(stateLocation)
	if err != nil {
		fmt.Printf("Error reading state.json: %s\n", err)
		os.Exit(1)
	}

	var inputState StateInput
	err = json.Unmarshal([]byte(inputText), &inputState)
	if err != nil {
		fmt.Printf("Error unmarshaling to JSON schema: %s\n", err)
		os.Exit(1)
	}
	return &inputState
}

func botLogic(currentState *StateInput) string {
	action := ""

	var botPosition = currentState.Player.Position
	var botEndPosition = Position{
		X: botPosition.X + currentState.Player.Speed,
		Y: botPosition.Y,
	}

	var mudAhead = false
	for _, lane := range currentState.WorldMap {
		for _, cell := range lane {
			var cellAheadX = cell.Position.X > botPosition.X && cell.Position.Y == botPosition.Y && cell.Position.X <= botEndPosition.X
			if cellAheadX && cell.SurfaceObject == 1 {
				mudAhead = true
			}
		}
	}

	if mudAhead {
		var canTurnLeft = botPosition.Y > 1
		var canTurnRight = botPosition.Y < 4
		if canTurnLeft {
			action = "TURN_LEFT"

		} else if canTurnRight {
			action = "TURN_RIGHT"
		}
	} else {
		action = "ACCELERATE"
	}

	return action
}
