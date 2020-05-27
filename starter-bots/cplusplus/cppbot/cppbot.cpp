#include <iostream>
#include "./rapidjson/document.h"
#include <fstream>
#include <string>
#include <sstream>
#include <vector>
#include <functional>
#include <cmath>
#include <random>

struct POINT
{
  int x;
  int y;
};

enum class SurfaceObject : uint8_t {
  EMPTY = 0,
  MUD = 1,
  OIL_SPILL = 2,
  OIL_ITEM = 3,
  FINISH_LINE = 4,
  BOOST = 5
};

SurfaceObject GetObjectAt(rapidjson::Document& roundJSON, POINT point)
{
  for (rapidjson::Value::ConstValueIterator rowItr = roundJSON["worldMap"].Begin(); rowItr != roundJSON["worldMap"].End(); ++rowItr) {
    for (rapidjson::Value::ConstValueIterator colItr = (*rowItr).Begin(); colItr != (*rowItr).End(); ++colItr) {

        if ((*colItr)["position"]["x"].GetInt() == point.x && (*colItr)["position"]["y"].GetInt() == point.y) {
          int obj_int = (*colItr)["surfaceObject"].GetInt();
          SurfaceObject obj = static_cast<SurfaceObject>(obj_int);
          return obj;
        }
    }
  }

  throw std::runtime_error("Invalue position provided to GetObjectAt");
}

std::string RandomStrategy(rapidjson::Document& roundJSON)
{
  std::random_device rd;  //Will be used to obtain a seed for the random number engine
  std::mt19937 gen(rd()); //Standard mersenne_twister_engine seeded with rd()
  std::uniform_int_distribution<> dis(0, 3);
  
  //access game state as follows:
  int myPlayerID = roundJSON["player"]["id"].GetInt();
  int myPlayerPosX = roundJSON["player"]["position"]["x"].GetInt();
  int myPlayerPosY = roundJSON["player"]["position"]["y"].GetInt();
  SurfaceObject objInFrontOfMe = GetObjectAt(roundJSON, {myPlayerPosX+1, myPlayerPosY});

  //(but this strategy doesn't use any of that...)
  switch(dis(gen)) {
    case 0:
      return "TURN_LEFT";
    case 1:
      return "TURN_RIGHT";
    case 2:
      return "ACCELERATE";
    case 3:
      return "DECELERATE";
  }
}

std::string runStrategy(rapidjson::Document& roundJSON)
{
  return RandomStrategy(roundJSON);
}

std::string executeRound(std::string& roundNumber)
{
  std::string ret;
  const std::string filePath = "./rounds/" + roundNumber + "/state.json";
  std::ifstream dataIn;
  dataIn.open(filePath, std::ifstream::in);
  if (dataIn.is_open())
  {
    std::stringstream buffer;
    buffer << dataIn.rdbuf();
    std::string stateJson = buffer.str();
    rapidjson::Document roundJSON;
    const bool parsed = !roundJSON.Parse(stateJson.c_str()).HasParseError();
    if (parsed)
    {
      ret = "C;" + roundNumber + ";" + runStrategy(roundJSON) + "\n";
    }
    else
    {
      ret = "C;" + roundNumber + ";error executeRound \n";
    }
  }

  return ret;
}

int main(int argc, char** argv)
{
	std::string roundNumber;
	
	while(true)
	{
		std::cin >> roundNumber;
		std::cout << executeRound(roundNumber);
	}
}
