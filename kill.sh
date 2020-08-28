#!/bin/sh
function PidFind() {

  PIDCOUNT=$(ps -ef | grep ai-sports)

  if [ ${PIDCOUNT} -gt 1 ]; then

    echo "There are too many process contains name[$1]"

  elif [ ${PIDCOUNT} -le 0 ]; then

    echo "No such process[ai-sports]!"

  else

    PID=$(ps -ef | grep ai-sports)

    echo "Find the PID of this progress!--- process:ai-sports PID=[${PID}] "

    echo "Kill the process ai-sports ..."

    kill -9 ${PID}

    echo "kill -9 ${PID} ai-sports done!"

  fi

}

PidFind $1

exit 1
