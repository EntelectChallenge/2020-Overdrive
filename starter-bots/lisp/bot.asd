(in-package :cl-user)

(defpackage :bot.sys
  (:use :asdf :cl))

(in-package :bot.sys)

(defsystem :bot
  :name "Bot"
  :author "John Doe"
  :version "0.0.1"
  :maintainer "john.doe@john.com"
  :license "BSD"
  :description "Entellect challenge bot"
  :long-description "Bot for the Entellect Challenge"
  :components ((:file "bot")))
