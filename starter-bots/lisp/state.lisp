(in-package :bot)

(define-poclo state (current-round
                     max-rounds
                     (player () player)
                     (opponent () opponent)
                     (world-map () map-tile)) 
  camel-case)

(define-poclo player (id (map-position "position" map-position) 
                         (player-speed "speed" ()) state powerups 
                         boosting boost-counter)
  camel-case)

(define-poclo map-position (x y) camel-case)

(define-poclo map-tile ((map-position "position" map-position)
                        surface-object occupied-by-player-id)
  camel-case)

(define-poclo opponent (id (map-position "position" map-position) 
                           (player-speed "speed" ()))
  camel-case)

