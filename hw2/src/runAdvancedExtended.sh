rm *.txt
rm Heat2D.class
rm Heat2DPAdvanced.class
rm Heat2DPBasic.class
javac Heat2D.java
javac Heat2DPAdvanced.java
javac Heat2DPBasic.java
java Heat2D 1000 1000 700 100 > o0.txt
mpirun -n 1 java Heat2DPAdvanced 1000 1000 700 100 > o1.txt
mpirun -n 2 java Heat2DPAdvanced 1000 1000 700 100 > o2.txt
mpirun -n 3 java Heat2DPAdvanced 1000 1000 700 100 > o3.txt
mpirun -n 4 java Heat2DPAdvanced 1000 1000 700 100 > o4.txt
mpirun -n 5 java Heat2DPAdvanced 1000 1000 700 100 > o5.txt


echo "o0 vs o1"
diff -s o0.txt o1.txt


echo ; echo ; echo 



echo "o0 vs o2"
diff -s o0.txt o2.txt 


echo ; echo ; echo 



echo "o0 vs o3"
diff -s o0.txt o3.txt


echo ; echo ; echo 



echo "o0 vs o4"
diff -s o0.txt o4.txt


echo ; echo ; echo 



echo "o0 vs o5"
diff -s o0.txt o5.txt


echo ; echo ; echo 



echo "o1 vs o5"
diff -s o1.txt o5.txt


echo ; echo ; echo 




echo "Test finished"

echo ; echo ; echo 
