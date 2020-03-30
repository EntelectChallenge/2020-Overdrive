(load "bot.asd")

(setf ql:*quickload-verbose* t)

(ql:quickload "bot")

(format t "Processor Architecture ~a~%" (machine-type))
(format t "SBCL version ~a~%" (software-version))
(format t "~a~%" *FEATURES*)

(save-lisp-and-die "bot" :toplevel #'bot:main :executable t)

