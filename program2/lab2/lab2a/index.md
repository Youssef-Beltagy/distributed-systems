
# Lab2a

As I increase the number of computing nodes, the turnaround time decreases. The number of computing nodes and the turnaround time are inversely related. However, the turnaround time doesn't improve as much as expected as we increase the number of computing nodes because of the overhead of communication.

## output:

```shell
[ybeltagy@cssmpi1h lab2a]$ CSSmpdboot -n 4 -v
Preparing...
Starting Master's mpd Process...
Starting node: cssmpi2h
Starting node: cssmpi3h
Starting node: cssmpi4h
Starting node: cssmpi5h
Starting node: cssmpi6h
Starting node: cssmpi7h
Starting node: cssmpi8h
Cluster built:
cssmpi1h.uwb.edu_41636 (10.158.82.61)
cssmpi8h.uwb.edu_42953 (10.158.82.68)
cssmpi2h.uwb.edu_44732 (10.158.82.62)
cssmpi6h.uwb.edu_41979 (10.158.82.66)
cssmpi4h.uwb.edu_39281 (10.158.82.64)
cssmpi3h.uwb.edu_44287 (10.158.82.63)
CSSmpdboot finished!
[ybeltagy@cssmpi1h lab2a]$ mpirun -n 1 java MatrixMult 3000 n
array a:
array b:
sending 3000 rows to rank 0
result c:
time elapsed = 37950 msec
rank[0] at cssmpi1h.uwb.edu: multiplication completed
[ybeltagy@cssmpi1h lab2a]$ mpirun -n 2 java MatrixMult 3000 n
array a:
array b:
sending 1500 rows to rank 0
sending 1500 rows to rank 1
result c:
time elapsed = 21290 msec
rank[0] at cssmpi1h.uwb.edu: multiplication completed
rank[1] at cssmpi7h.uwb.edu: multiplication completed
[ybeltagy@cssmpi1h lab2a]$ mpirun -n 3 java MatrixMult 3000 n
array a:
array b:
sending 1000 rows to rank 0
sending 1000 rows to rank 1
sending 1000 rows to rank 2
rank[1] at cssmpi7h.uwb.edu: multiplication completed
result c:
time elapsed = 17354 msec
rank[0] at cssmpi1h.uwb.edu: multiplication completed
rank[2] at cssmpi5h.uwb.edu: multiplication completed
[ybeltagy@cssmpi1h lab2a]$ mpirun -n 4 java MatrixMult 3000 n
array a:
array b:
sending 750 rows to rank 0
sending 750 rows to rank 1
sending 750 rows to rank 2
sending 750 rows to rank 3
rank[1] at cssmpi7h.uwb.edu: multiplication completed
rank[2] at cssmpi5h.uwb.edu: multiplication completed
result c:
time elapsed = 15871 msec
rank[0] at cssmpi1h.uwb.edu: multiplication completed
rank[3] at cssmpi8h.uwb.edu: multiplication completed
[ybeltagy@cssmpi1h lab2a]$

```

