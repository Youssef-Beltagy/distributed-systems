#java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent P 1 cssmpi2h 4 who ls ps df
#java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent P 2 cssmpi2h cssmpi3h 4 who ls ps df
#java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent P 3 cssmpi2h cssmpi3h cssmpi4h 4 who ls ps df
#java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent P 1 cssmpi2h 12 who ls ps df who ls ps df who ls ps df
#java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent P 2 cssmpi2h cssmpi3h 12 who ls ps df who ls ps df who ls ps df
#java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent P 3 cssmpi2h cssmpi3h cssmpi4h 12 who ls ps df who ls ps df who ls ps df

java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent C 1 cssmpi2h 1 grep\ -o\ 123\ ../files/text1.txt
java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent C 2 cssmpi2h cssmpi3h 1 grep\ -o\ 123\ ../files/text1.txt
java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent C 3 cssmpi2h cssmpi3h cssmpi4h 1 grep\ -o\ 123\ ../files/text1.txt

java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent C 1 cssmpi2h 4 who ls ps df
java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent C 2 cssmpi2h cssmpi3h 4 who ls ps df
java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent C 3 cssmpi2h cssmpi3h cssmpi4h 4 who ls ps df
java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent C 1 cssmpi2h 12 who ls ps df who ls ps df who ls ps df
java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent C 2 cssmpi2h cssmpi3h 12 who ls ps df who ls ps df who ls ps df
java -cp UWAgent.jar:. UWAgent.UWInject -p 28540 localhost UnixAgent C 3 cssmpi2h cssmpi3h cssmpi4h 12 who ls ps df who ls ps df who ls ps df


