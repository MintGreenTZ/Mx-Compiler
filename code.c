/*
Test Package: Codegen
Author: Pikachu
Time: 2020-02-02
Input:
=== input ===
79 35 12
=== end ===
Output:
=== output ===
<< 23 24 25 26 27 28 29 30 31 32 33 34 (35) 36 37 38 39 40 41 42 43 44 45 46 47 >>
=== end ===
ExitCode: 0
InstLimit: -1
Origin Package: Codeforces 399A #44003944
*/
#include <stdio.h>
int n = 10;
int p = 5;
int k = 3;
int i;
int main() {

	if(p-k>1) printf("<< ");
	i = p - k;

    if(i>=1)
     {
        printf("%d", i);
        printf(" ");
    }
	if(p + k < n) printf(">> ");

	return 0;
}