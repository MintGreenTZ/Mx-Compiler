	.file	"builtin.c"
	.option nopic
	.text
	.section	.rodata.str1.4,"aMS",@progbits,1
	.align	2
.LC0:
	.string	"%s"
	.text
	.align	2
	.globl	print
	.type	print, @function
print:
	addi	sp,sp,-16
	sw	ra,12(sp)
	mv	a1,a0
	lui	a0,%hi(.LC0)
	addi	a0,a0,%lo(.LC0)
	call	printf
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	print, .-print
	.align	2
	.globl	println
	.type	println, @function
println:
	addi	sp,sp,-16
	sw	ra,12(sp)
	call	puts
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	println, .-println
	.section	.rodata.str1.4
	.align	2
.LC1:
	.string	"%d"
	.text
	.align	2
	.globl	printInt
	.type	printInt, @function
printInt:
	addi	sp,sp,-16
	sw	ra,12(sp)
	mv	a1,a0
	lui	a0,%hi(.LC1)
	addi	a0,a0,%lo(.LC1)
	call	printf
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	printInt, .-printInt
	.section	.rodata.str1.4
	.align	2
.LC2:
	.string	"%d\n"
	.text
	.align	2
	.globl	printlnInt
	.type	printlnInt, @function
printlnInt:
	addi	sp,sp,-16
	sw	ra,12(sp)
	mv	a1,a0
	lui	a0,%hi(.LC2)
	addi	a0,a0,%lo(.LC2)
	call	printf
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	printlnInt, .-printlnInt
	.align	2
	.globl	getString
	.type	getString, @function
getString:
	addi	sp,sp,-16
	sw	ra,12(sp)
	sw	s0,8(sp)
	li	a0,256
	call	malloc
	mv	s0,a0
	mv	a1,a0
	lui	a0,%hi(.LC0)
	addi	a0,a0,%lo(.LC0)
	call	__isoc99_scanf
	mv	a0,s0
	lw	ra,12(sp)
	lw	s0,8(sp)
	addi	sp,sp,16
	jr	ra
	.size	getString, .-getString
	.align	2
	.globl	getInt
	.type	getInt, @function
getInt:
	addi	sp,sp,-32
	sw	ra,28(sp)
	addi	a1,sp,12
	lui	a0,%hi(.LC1)
	addi	a0,a0,%lo(.LC1)
	call	__isoc99_scanf
	lw	a0,12(sp)
	lw	ra,28(sp)
	addi	sp,sp,32
	jr	ra
	.size	getInt, .-getInt
	.align	2
	.globl	toString
	.type	toString, @function
toString:
	addi	sp,sp,-16
	sw	ra,12(sp)
	sw	s0,8(sp)
	beq	a0,zero,.L26
	mv	s0,a0
	li	a0,12
	call	malloc
	li	a3,0
	blt	s0,zero,.L27
.L16:
	li	a5,1000001536
	addi	a5,a5,-1537
	bgt	s0,a5,.L22
	li	a5,1000001536
	addi	a5,a5,-1536
	li	a2,10
.L18:
	mv	a4,a5
	div	a5,a5,a2
	blt	s0,a5,.L18
	li	a2,9
	ble	a4,a2,.L19
.L17:
	li	a6,10
	li	a1,9
.L20:
	addi	a3,a3,1
	add	a2,a0,a3
	div	a4,s0,a5
	addi	a4,a4,48
	sb	a4,-1(a2)
	rem	s0,s0,a5
	mv	a4,a5
	div	a5,a5,a6
	bgt	a4,a1,.L20
.L19:
	add	a3,a0,a3
	sb	zero,0(a3)
.L13:
	lw	ra,12(sp)
	lw	s0,8(sp)
	addi	sp,sp,16
	jr	ra
.L26:
	li	a0,2
	call	malloc
	li	a5,48
	sb	a5,0(a0)
	sb	zero,1(a0)
	j	.L13
.L27:
	neg	s0,s0
	li	a5,45
	sb	a5,0(a0)
	li	a3,1
	j	.L16
.L22:
	li	a5,1000001536
	addi	a5,a5,-1536
	j	.L17
	.size	toString, .-toString
	.align	2
	.globl	string_length
	.type	string_length, @function
string_length:
	addi	sp,sp,-16
	sw	ra,12(sp)
	call	strlen
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	string_length, .-string_length
	.align	2
	.globl	string_substring
	.type	string_substring, @function
string_substring:
	addi	sp,sp,-32
	sw	ra,28(sp)
	sw	s0,24(sp)
	sw	s1,20(sp)
	sw	s2,16(sp)
	sw	s3,12(sp)
	mv	s3,a0
	mv	s2,a1
	sub	s0,a2,a1
	addi	a0,s0,1
	call	malloc
	mv	s1,a0
	mv	a2,s0
	add	a1,s3,s2
	call	memcpy
	add	s0,s1,s0
	sb	zero,0(s0)
	mv	a0,s1
	lw	ra,28(sp)
	lw	s0,24(sp)
	lw	s1,20(sp)
	lw	s2,16(sp)
	lw	s3,12(sp)
	addi	sp,sp,32
	jr	ra
	.size	string_substring, .-string_substring
	.align	2
	.globl	string_parseInt
	.type	string_parseInt, @function
string_parseInt:
	addi	sp,sp,-32
	sw	ra,28(sp)
	addi	a2,sp,12
	lui	a1,%hi(.LC1)
	addi	a1,a1,%lo(.LC1)
	call	__isoc99_sscanf
	lw	a0,12(sp)
	lw	ra,28(sp)
	addi	sp,sp,32
	jr	ra
	.size	string_parseInt, .-string_parseInt
	.align	2
	.globl	string_ord
	.type	string_ord, @function
string_ord:
	add	a0,a0,a1
	lbu	a0,0(a0)
	ret
	.size	string_ord, .-string_ord
	.align	2
	.globl	string_add
	.type	string_add, @function
string_add:
	addi	sp,sp,-16
	sw	ra,12(sp)
	sw	s0,8(sp)
	sw	s1,4(sp)
	sw	s2,0(sp)
	mv	s2,a0
	mv	s1,a1
	call	strlen
	mv	s0,a0
	mv	a0,s1
	call	strlen
	add	a0,s0,a0
	addi	a0,a0,1
	call	malloc
	mv	s0,a0
	mv	a1,s2
	call	strcpy
	mv	a1,s1
	mv	a0,s0
	call	strcat
	mv	a0,s0
	lw	ra,12(sp)
	lw	s0,8(sp)
	lw	s1,4(sp)
	lw	s2,0(sp)
	addi	sp,sp,16
	jr	ra
	.size	string_add, .-string_add
	.align	2
	.globl	string_eq
	.type	string_eq, @function
string_eq:
	addi	sp,sp,-16
	sw	ra,12(sp)
	call	strcmp
	seqz	a0,a0
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	string_eq, .-string_eq
	.align	2
	.globl	string_neq
	.type	string_neq, @function
string_neq:
	addi	sp,sp,-16
	sw	ra,12(sp)
	call	strcmp
	snez	a0,a0
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	string_neq, .-string_neq
	.align	2
	.globl	string_lt
	.type	string_lt, @function
string_lt:
	addi	sp,sp,-16
	sw	ra,12(sp)
	call	strcmp
	srli	a0,a0,31
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	string_lt, .-string_lt
	.align	2
	.globl	string_le
	.type	string_le, @function
string_le:
	addi	sp,sp,-16
	sw	ra,12(sp)
	call	strcmp
	slti	a0,a0,1
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	string_le, .-string_le
	.align	2
	.globl	string_gt
	.type	string_gt, @function
string_gt:
	addi	sp,sp,-16
	sw	ra,12(sp)
	call	strcmp
	sgt	a0,a0,zero
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	string_gt, .-string_gt
	.align	2
	.globl	string_ge
	.type	string_ge, @function
string_ge:
	addi	sp,sp,-16
	sw	ra,12(sp)
	call	strcmp
	not	a0,a0
	srli	a0,a0,31
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
	.size	string_ge, .-string_ge
	.ident	"GCC: (GNU) 9.2.0"
	.section	.note.GNU-stack,"",@progbits
