(defpackage :bot
  (:use :cl)
  (:export #:main))

(in-package :bot)

(defun main () 
  (loop while t
     for round-number = (read-line)
     do (format t "C;~a;ACCELERATE~%" round-number)))

