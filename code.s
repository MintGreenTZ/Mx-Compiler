	.file	"code.c"
	.option nopic
	.text
	.section	.rodata.str1.4,"aMS",@progbits,1
	.align	2
.LC0:
	.string	"<< "
	.align	2
.LC1:
	.string	"%d"
	.align	2
.LC2:
	.string	">> "
	.text
	.align	2
	.globl	main
	.type	main, @function
main:
	addi	sp,sp,-16
	sw	ra,12(sp)
	lui	a5,%hi(p)
	lw	a5,%lo(p)(a5)
	lui	a4,%hi(k)
	lw	a4,%lo(k)(a4)
	sub	a5,a5,a4
	li	a4,1
	bgt	a5,a4,.L6
.L2:
	lui	a5,%hi(p)
	lw	a1,%lo(p)(a5)
	lui	a5,%hi(k)
	lw	a5,%lo(k)(a5)
	sub	a1,a1,a5
	lui	a5,%hi(i)
	sw	a1,%lo(i)(a5)
	bgt	a1,zero,.L7
.L3:
	lui	a5,%hi(p)
	lw	a5,%lo(p)(a5)
	lui	a4,%hi(k)
	lw	a4,%lo(k)(a4)
	add	a5,a5,a4
	lui	a4,%hi(n)
	lw	a4,%lo(n)(a4)
	blt	a5,a4,.L8
.L4:
	li	a0,0
	lw	ra,12(sp)
	addi	sp,sp,16
	jr	ra
.L6:
	lui	a0,%hi(.LC0)
	addi	a0,a0,%lo(.LC0)
	call	printf
	j	.L2
.L7:
	lui	a0,%hi(.LC1)
	addi	a0,a0,%lo(.LC1)
	call	printf
	li	a0,32
	call	putchar
	j	.L3
.L8:
	lui	a0,%hi(.LC2)
	addi	a0,a0,%lo(.LC2)
	call	printf
	j	.L4
	.size	main, .-main
	.comm	i,4,4
	.globl	k
	.globl	p
	.globl	n
	.section	.sdata,"aw"
	.align	2
	.type	k, @object
	.size	k, 4
k:
	.word	3
	.type	p, @object
	.size	p, 4
p:
	.word	5
	.type	n, @object
	.size	n, 4
n:
	.word	10
	.ident	"GCC: (GNU) 9.2.0"
	.section	.note.GNU-stack,"",@progbits
