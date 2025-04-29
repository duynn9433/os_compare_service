git clone https://github.com/hazelcast/hazelcast-simulator.git
git checkout tag/0.13
unzip hazelcast-simulator-0.13-dist.zip
cd hazelcast-simulator-0.13

./bin/simulator-wizard --install
simulator-wizard --createWorkDir myFirstTest
cd myFirstTest

