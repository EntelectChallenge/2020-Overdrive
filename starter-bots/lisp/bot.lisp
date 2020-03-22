(in-package :bot)

(defun main () 
  (loop while t
     for round-number = (read-line)
     for state = (with-open-file (f (format nil "rounds/~a/state.json" round-number))
                   (parse-state f))
     do (format t "C;~a;ACCELERATE~%" (current-round state))))

