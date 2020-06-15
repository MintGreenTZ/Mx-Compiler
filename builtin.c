#include<string.h>
#include<stdio.h>
#include<stdlib.h> 

void print(char *s) {
	printf("%s", s);
}

void println(char *s) {
	printf("%s\n", s);
}

void printInt(char *s) {
	printf("%d", s);
}

void printlnInt(char *s) {
	printf("%d\n", s);
}

char* getString(){
	char* ptr = malloc(256);
	scanf("%s", ptr);
	return ptr;
}

int getInt(){
	int x;
	scanf("%d", &x);
	return x;
}

char* toString(int x){
	if(x == 0){
		char *ptr = malloc(2);
		ptr[0] = '0';
		ptr[1] = '\0';
		return ptr;
	}
	char* ptr = malloc(12);
	int cur = 0;
	if(x < 0){
		x = -x;
		ptr[cur++] = '-';
	}
	int base = 1000000000;
	while(base > x)
		base /= 10;
	while(base > 0){
		ptr[cur++] = x / base + '0';
		x %= base;
		base /= 10;
	}
	ptr[cur++] = '\0';
	return ptr;
}	

int string_length(char *s){
	return strlen(s);
}

char* string_substring(char *ptr, int left, int right){
	char *new_ptr = malloc(right - left + 1);
	memcpy(new_ptr, ptr + left, right - left);
	new_ptr[right - left] = '\0';
	return new_ptr;
}

int string_parseInt(char *ptr){
	int x;
	sscanf(ptr, "%d", &x);
	return x;
}

int string_ord(char *ptr, int idx){
	return ptr[idx];
}

char* string_add(char *ptr1, char *ptr2){
	int len1 = strlen(ptr1);
	int len2 = strlen(ptr2);
	char* new_ptr = malloc(len1+len2+1);
	strcpy(new_ptr, ptr1);
	strcat(new_ptr, ptr2);
	return new_ptr;
}

int string_eq(char *s1, char *s2){
	return strcmp(s1, s2) == 0;
}
int string_neq(char *s1, char *s2){
	return strcmp(s1, s2) != 0;
}
int string_lt(char *s1, char *s2){
	return strcmp(s1, s2) < 0;
}
int string_le(char *s1, char *s2){
	return strcmp(s1, s2) <= 0;
}
int string_gt(char *s1, char *s2){
	return strcmp(s1, s2) > 0;
}
int string_ge(char *s1, char *s2){
	return strcmp(s1, s2) >= 0;
}

