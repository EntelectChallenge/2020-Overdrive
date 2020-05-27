package main

type Position struct {
	Y int `json:"y"`
	X int `json:"x"`
}

type Player struct {
	ID           int      `json:"id"`
	Position     Position `json:"position"`
	Speed        int      `json:"speed"`
	State        string   `json:"state"`
	Powerups     []string `json:"powerups"`
	Boosting     bool     `json:"boosting"`
	BoostCounter int      `json:"boostCounter"`
}

type Opponent struct {
	ID       int      `json:"id"`
	Position Position `json:"position"`
	Speed    int      `json:"speed"`
}

type MapCell struct {
	Position           Position `json:"position"`
	SurfaceObject      int      `json:"surfaceObject"`
	OccupiedByPlayerID int      `json:"occupiedByPlayerId"`
}

type StateInput struct {
	CurrentRound int         `json:"currentRound"`
	MaxRounds    int         `json:"maxRounds"`
	Player       Player      `json:"player"`
	Opponent     Opponent    `json:"opponent"`
	WorldMap     [][]MapCell `json:"worldMap"`
}
