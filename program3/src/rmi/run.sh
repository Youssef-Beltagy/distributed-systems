java UnixClient P 28540 1 cssmpi2h 4 who ls ps df > output1.txt
java UnixClient P 28540 2 cssmpi2h cssmpi3h 4 who ls ps df >> output1.txt
java UnixClient P 28540 3 cssmpi2h cssmpi3h cssmpi4h 4 who ls ps df >> output1.txt
java UnixClient P 28540 1 cssmpi2h 12 who ls ps df who ls ps df who ls ps df >> output1.txt
java UnixClient P 28540 2 cssmpi2h cssmpi3h 12 who ls ps df who ls ps df who ls ps df >> output1.txt
java UnixClient P 28540 3 cssmpi2h cssmpi3h cssmpi4h 12 who ls ps df who ls ps df who ls ps df >> output1.txt

java UnixClient C 28540 1 cssmpi2h 1 grep -o 123 ../files/text1.txt > output2.txt
java UnixClient C 28540 2 cssmpi2h cssmpi3h 1 grep -o 123 ../files/text1.txt >> output2.txt
java UnixClient C 28540 3 cssmpi2h cssmpi3h cssmpi4h 1 grep -o 123 ../files/text1.txt >> output2.txt

java UnixClient P 28540 1 cssmpi2h 1 cat ../files/text1.txt | grep -o 123 | wc -l > output3.txt
java UnixClient P 28540 2 cssmpi2h cssmpi3h 1 cat ../files/text1.txt | grep -o 123 | wc -l >> output3.txt
java UnixClient P 28540 3 cssmpi2h cssmpi3h cssmpi4h 1 cat ../files/text1.txt | grep -o 123 | wc -l >> output3.txt


