if [ ! -f "TestServer/spigot.jar" ]; then
  mkdir -p "TestServer/"
  cp "$HOME/.m2/repository/org/spigotmc/spigot/1.14.4-R0.1-SNAPSHOT/spigot-1.14.4-R0.1-SNAPSHOT.jar" "TestServer/spigot.jar"
  cd ../
fi
gradle jar
mkdir -p "TestServer/plugins"
cp "build/libs/fortuneblocks-2.2.jar" "TestServer/plugins/FortuneBlocks.jar"
cd TestServer || exit
if [ ! -f "eula.txt" ]; then
  java -jar "spigot.jar"
  echo "eula=true" >eula.txt
fi
java -jar "spigot.jar"
