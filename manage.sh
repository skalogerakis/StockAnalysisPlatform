#!/bin/bash

# HOME=`eval echo ~$USER`
HOME="/media/localdisk/skalogerakis"
REPO_HOME=$HOME
DOWNLOADS="${REPO_HOME}/DOWNLOADS"
KAFKA_HOME="${REPO_HOME}/kafka_2.12-3.3.2"
FLINK_HOME="${REPO_HOME}/flink"
FLINK_CHECKPOINT_DIR="${REPO_HOME}/flink_checkpoint"

USAGE_MSG="$0 <install, stop, start>"

PARALLELISM=1
J1_ARG=0
J2_ARG=1
CHECKPOINT_INTERVAL=-1
REPORT_MODE=0   #0=report q1 and q2, 1=report q1, 2=report q2

function help() {
    echo "Syntax: $0 install| start [-p parallelim] [-i inteval1] [-j interval2] | stop"
    echo "options:"
    echo "install   Intall the necessary software stack (utilities, processing platforms)"
    echo "build     Build application from source code"
    echo "start     Deploy and start the processes for fetching and analysing data"
    echo "      optional start arguments:"
    echo "      -p <number>  Flink parallelism (Default: 1)"
    echo "      -i <number>  Interval 1 (Default 38)"
    echo "      -j <number>  Interval 2 (Default 100)"
    echo "      -c <minutes> Checkpointing interval in minutes (Default: no checkpointing)" 
    echo "      -q <number>  Specify the reported queries. 1 for Q1, 2 for Q2. (Default report both queries)"
    echo "stop      Stops processing and processing platform"
    echo ""
    echo "e.g. ./manage.sh start -p 2 -i 50 -j 90 -q 1"
}

function install_utilities() {
    echo "$(date +'%d/%m/%y %T') Install necessary dependencies. This may take a while. Please wait"
    #    echo $(hostname -I | cut -d\  -f1) $(hostname) | sudo tee -a /etc/hosts
    sudo apt-get update > /dev/null 2>&1
    sudo apt-get install -y htop build-essential openjdk-8-jdk maven git > /dev/null 2>&1
    sudo timedatectl set-timezone Europe/Athens
	cd ${REPO_HOME}
    mkdir -p $DOWNLOADS
    mkdir -p ${FLINK_CHECKPOINT_DIR}
}


function flink_install() {
    echo "$(date +'%d/%m/%y %T') Install Flink"
    #    cd ${REPO_HOME}/${DOWNLOADS}
    cd ${DOWNLOADS}
    wget --quiet https://archive.apache.org/dist/flink/flink-1.14.3/flink-1.14.3-bin-scala_2.12.tgz
    tar -zxvf flink-1.14.3-bin-scala_2.12.tgz > /dev/null 2>&1

    cd ${REPO_HOME}
    ln -sf ${DOWNLOADS}/flink-1.14.3 ${FLINK_HOME}
    
    flink_config
}

# FIXME ideally add config for the whole cluster
function flink_config() {
    #JM_IP=$(hostname -I | cut -d\  -f1)
    #sed -i -e "/jobmanager\.rpc\.address:/ s/: .*/: ${JM_IP}/" ${FLINK_HOME}/conf/flink-conf.yaml
    sed -i -e "/taskmanager\.memory\.process\.size:/ s/: .*/: 5000m/" ${FLINK_HOME}/conf/flink-conf.yaml
    sed -i -e "/taskmanager\.numberOfTaskSlots:/ s/: .*/: ${PARALLELISM}/" ${FLINK_HOME}/conf/flink-conf.yaml 
}

# start/stop flink job manager
flink_manage_jm() {
    if [ $# -lt 1 ]; then
        echo "Wrong number of arguments for jobmanager!"
        echo "Params should be start/stop"
        exit 1
    fi

    if [ "$1" != "start" ] && [ "$1" != "stop" ] ;
    then
        echo "Wrong arguments for jomanager"
        exit 1
    fi

    echo "jobmanager " $1
    cd ${FLINK_HOME}/bin && ./jobmanager.sh $1
}

# start/stop flink task manager
flink_manage_tm() {
    if [ $# -lt 1 ]; then
        echo "Wrong number of arguments for jobmanager!"
        echo "Params should be start/stop"
        exit 1
    fi

    if [ "$1" != "start" ] && [ "$1" != "stop" ] ;
    then
        echo "Wrong arguments for jomanager"
        exit 1
    fi

    echo "taskmanager " $1

    cd ${FLINK_HOME}/bin && ./taskmanager.sh $1
}

flink_cluster_start() {
    echo "Starting Flink Cluster "

    cd ${FLINK_HOME}/bin && ./start-cluster.sh $1
}

flink_cluster_stop() {
    echo "Stopping Flink Cluster "

    cd ${FLINK_HOME}/bin && ./stop-cluster.sh $1
}

function flink_clean() {
    echo "$(date +'%d/%m/%y %T') Flink clean logs"
    rm -rf ${FLINK_HOME}/log/*
}

function kafka_install() {
    echo "$(date +'%d/%m/%y %T') Install Kafka"
    cd ${DOWNLOADS}
    wget --quiet --no-check-certificate https://dlcdn.apache.org/kafka/3.3.2/kafka_2.12-3.3.2.tgz
    cd ${REPO_HOME}
    tar -zxvf ${DOWNLOADS}/kafka_2.12-3.3.2.tgz > /dev/null 2>&1
    echo "transaction.max.timeout.ms=90000000" >> kafka_2.12-3.3.2/config/server.properties
}

function kafka_start() {
    echo "$(date +'%d/%m/%y %T') Start Kafka"
    # start zookeeper
    ${KAFKA_HOME}/bin/zookeeper-server-start.sh -daemon ${KAFKA_HOME}/config/zookeeper.properties
    sleep 2
    # start kafka server
    ${KAFKA_HOME}/bin/kafka-server-start.sh -daemon ${KAFKA_HOME}/config/server.properties 
    sleep 3
}

function kafka_stop() {
    echo "$(date +'%d/%m/%y %T') Stop Kafka"
    ${KAFKA_HOME}/bin/kafka-server-stop.sh
    ${KAFKA_HOME}/bin/zookeeper-server-stop.sh
}

function kafka_create_topics() {
    echo "$(date +'%d/%m/%y %T') Create Kafka topics"
    # ${KAFKA_HOME}/bin/kafka-topics.sh --create --topic topic --partitions 32 --replication-factor 1 --bootstrap-server localhost:9092
    ${KAFKA_HOME}/bin/kafka-topics.sh --create --topic topic-serial --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092
    ${KAFKA_HOME}/bin/kafka-topics.sh --create --topic topic-parallel --partitions 32 --replication-factor 1 --bootstrap-server localhost:9092
    # ${KAFKA_HOME}/bin/kafka-topics.sh --create --topic topicQ1 --partitions 32 --replication-factor 1 --bootstrap-server localhost:9092
    # ${KAFKA_HOME}/bin/kafka-topics.sh --create --topic topicQ2 --partitions 32 --replication-factor 1 --bootstrap-server localhost:9092
}


function kafka_delete_q1_q2() {
    echo "$(date +'%d/%m/%y %T') Delete Kafka topics for Q1 & Q2"
    ${KAFKA_HOME}/bin/kafka-topics.sh --delete --topic topicQ1 --bootstrap-server localhost:9092
    ${KAFKA_HOME}/bin/kafka-topics.sh --delete --topic topicQ2 --bootstrap-server localhost:9092
}


function kafka_create_q1_q2() {
    echo "$(date +'%d/%m/%y %T') Re-create Kafka topics for Q1 & Q2"
    ${KAFKA_HOME}/bin/kafka-topics.sh --create --topic topicQ1 --partitions 32 --replication-factor 1 --bootstrap-server localhost:9092
    ${KAFKA_HOME}/bin/kafka-topics.sh --create --topic topicQ2 --partitions 32 --replication-factor 1 --bootstrap-server localhost:9092
}

function kafka_clean() {
    echo "$(date +'%d/%m/%y %T') Kafka clean"
    rm -rf /tmp/zookeeper
    rm -rf /tmp/kafka-logs
    rm -rf ${KAFKA_HOME}/logs/*
}

# function kafka_backup(){
#     echo "$(date +'%d/%m/%y %T') Kafka backup"
#     tar -czf ${KAFKA_HOME}/zookeeper-backup.tar.gz -C ${KAFKA_CONF}/zookeeper/ .
#     echo "$(date +'%d/%m/%y %T') Zookeeper state backup complete"
#     sleep 2
#     ${KAFKA_HOME}/bin/kafka-server-stop.sh
#     sleep 4 
#     tar -czf ${KAFKA_HOME}/kafka-backup.tar.gz -C ${KAFKA_CONF}/kafka-logs/ .
#     echo "$(date +'%d/%m/%y %T') Kafka Logs state backup complete"
#     ${KAFKA_HOME}/bin/zookeeper-server-stop.sh
# }

# function kafka_restore(){
#     echo "$(date +'%d/%m/%y %T') Kafka restore"

#     tar -C ${KAFKA_CONF}/zookeeper/ -xzf ${KAFKA_HOME}/zookeeper-backup.tar.gz --strip-components 1
#     echo "$(date +'%d/%m/%y %T') Zookeeper state restore complete"
#     sleep 2
#     tar -C ${KAFKA_CONF}/kafka-logs/ -xzf ${KAFKA_HOME}/kafka-backup.tar.gz --strip-components 1
#     echo "$(date +'%d/%m/%y %T') Kafka Logs state restore complete"
# }

function application_build() {
    echo "$(date +'%d/%m/%y %T') Build binaries"

    # use a predefined folder containig src code for TESTING
    DATA_LOADER_HOME=${REPO_HOME}/DataIngestionCSV
    cd ${DATA_LOADER_HOME}
    mvn clean package

    FLINK_JOB=${REPO_HOME}/StockAnalysisOptUpt
    cd ${FLINK_JOB}
    mvn clean package

    FLINK_JOB=${REPO_HOME}/StockAnalysisApp
    cd ${FLINK_JOB}
    mvn clean package
}

function ingest_job_start() {
    echo "$(date +'%d/%m/%y %T') Start ingesting data"
	# cd ${REPO_HOME}
 #    BINARY=${REPO_HOME}/DataIngestionCSV/target/DataIngestionCSV-1.0-SNAPSHOT-jar-with-dependencies.jar
 #    nohup java -jar ${BINARY} ${REPORT_MODE} > ingest.log 2>&1 &

    APP_BIN="${REPO_HOME}/DataIngestionCSV/target/DataIngestionCSV-0.1.jar"
    APP_PARAMS="${HOME}/debs2022-gc-trading-day-08-11-21.csv"
    ${FLINK_HOME}/bin/flink run -d -p ${PARALLELISM} ${APP_BIN} ${APP_PARAMS}
}

function flink_job_start() {
    echo "$(date +'%d/%m/%y %T') Start flink job"
#    PARALLELISM=1
    APP_BIN="${REPO_HOME}/StockAnalysisOptUpt/target/StockAnalysisOptUpt-0.1.jar"
    APP_PARAMS="${J1_ARG} ${J2_ARG} ${CHECKPOINT_INTERVAL} ${FLINK_CHECKPOINT_DIR}"
    ${FLINK_HOME}/bin/flink run -d -p ${PARALLELISM} ${APP_BIN} ${APP_PARAMS}
}

function flink_serial_job_start() {
    echo "$(date +'%d/%m/%y %T') Start flink job"
    APP_BIN="${REPO_HOME}/StockAnalysisApp/target/StockAnalysisApp-0.1.jar"
    APP_PARAMS="${J1_ARG} ${J2_ARG} ${CHECKPOINT_INTERVAL} ${FLINK_CHECKPOINT_DIR}"
    ${FLINK_HOME}/bin/flink run -d -p ${PARALLELISM} ${APP_BIN} ${APP_PARAMS}
}

function platform_start() {
	kafka_start
	sleep 5
	kafka_create_topics

	flink_manage_jm start
	flink_manage_tm start
}

function platform_stop() {
	kafka_stop
	sleep 2
	kafka_clean
	flink_manage_jm stop
	flink_manage_tm stop
}

function parse_start_args() {
#    shift
    
    while getopts p:i:j:c:q: opt; do
        case $opt in
            p)
                PARALLELISM=$OPTARG
            ;;
            i)
                J1_ARG=$OPTARG
                ;;
            j)
                J2_ARG=$OPTARG
                ;;
            c)
                CHECKPOINT_INTERVAL=$OPTARG
                ;;
            q)
                REPORT_MODE=$OPTARG
                ;;
            \?) 
                echo "Invalid argument"
                echo ""
                help
                exit;
            ;;
        esac
    done
#    echo "-- IN -- par: $PARALLELISM j1: $J1_ARG, j2: $J2_ARG"
}



# Check num of arguments
if [ $# -lt 1 ]; then
    echo "Wrong arguments!"
#    echo $USAGE_MSG
    help
  exit 1
fi

ACTION=$1

case "$ACTION" in
    install)
    	install_utilities
	    kafka_install
    	flink_install
        exit
	    ;;
    build)
        application_build
        exit
        ;;
    start)
        shift   # ignre "start" parameter and parse next params
        parse_start_args "$@"
        echo "par: $PARALLELISM j1: $J1_ARG, j2: $J2_ARG, c: $CHECKPOINT_INTERVAL, q: $REPORT_MODE"
        #    	application_build
	    sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
    	kafka_start
	    sleep 8
    	kafka_create_topics
        # flink_config
	    flink_manage_jm start
    	flink_manage_tm start
    	sleep 3
	    flink_job_start
    	sleep 10
	    ingest_job_start
        exit
    	;;
    process)    # process runs the processing app, must have already executed the ingest and ingest-stop
        shift   # ignore "start" parameter and parse next params
        parse_start_args "$@"
        echo "par: $PARALLELISM j1: $J1_ARG, j2: $J2_ARG, c: $CHECKPOINT_INTERVAL, q: $REPORT_MODE"
        sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
        # flink_config
        #kafka_create_q1_q2
        flink_cluster_start
        sleep 3
        flink_job_start
        exit
        ;;
    process-stop)
        flink_cluster_stop
        #kafka_delete_q1_q2
        sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
        flink_clean
        exit
        ;;
    serial)    
        shift   
        parse_start_args "$@"
        echo "par: $PARALLELISM j1: $J1_ARG, j2: $J2_ARG, c: $CHECKPOINT_INTERVAL, q: $REPORT_MODE"
        sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
        # flink_config
        #kafka_create_q1_q2
        flink_cluster_start
        sleep 3
        flink_serial_job_start
        exit
        ;;
    serial-stop)
        flink_cluster_stop
        #kafka_delete_q1_q2
        sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
        flink_clean
        exit
        ;;         
    stop)       #Stops both kafka and flink TM and JM
    	kafka_stop
	    sleep 2
    	kafka_clean
	    flink_manage_jm stop
	    flink_manage_tm stop
	    sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
    	flink_clean
        exit
    	;;
    # backup)
    #     kafka_backup
    #     sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
    #     exit
    #     ;;
    # restore)
    #     kafka_stop
    #     sleep 2
    #     kafka_clean
    #     sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
    #     kafka_restore
    #     sleep 4
    #     kafka_start
    #     exit
    #     ;;
    kafka-start)            # Starts kafka and creates topics
        sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
        kafka_start
        sleep 8
        kafka_create_topics
        exit
        ;;
    kafka-stop)             # Stops Kafka and deletes
        kafka_stop
        sleep 2
        kafka_clean
        sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
        exit
        ;;        
    ingest)                 # Creates topics and starts ingest job
        shift   
        parse_start_args "$@"
        echo "par: $PARALLELISM j1: $J1_ARG, j2: $J2_ARG, c: $CHECKPOINT_INTERVAL, q: $REPORT_MODE"
        sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
        kafka_start
        sleep 8
        kafka_create_topics
        flink_cluster_start
        sleep 10
        ingest_job_start
        exit
        ;;
    ingest-stop)            # Stops only the ingest job
        flink_cluster_stop
        sudo sh -c "sync; echo 3 > /proc/sys/vm/drop_caches "
        flink_clean
        exit
        ;;    
    *)
        echo "Unknown argument $ACTION"
        echo ""
        help
        exit
        ;;
esac

