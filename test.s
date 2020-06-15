	.section	.data

	.globl	init_anger
init_anger:
	.zero	4

	.globl	work_anger
work_anger:
	.zero	4

	.globl	unnamed_5
unnamed_5:
	.string	", "

	.globl	unnamed_10
unnamed_10:
	.string	" enjoys this work. XD"

	.globl	unnamed_12
unnamed_12:
	.string	", "

	.globl	unnamed_17
unnamed_17:
	.string	" wants to give up!!!!!"

	.globl	unnamed_26
unnamed_26:
	.string	"the leading TA"

	.globl	unnamed_30
unnamed_30:
	.string	"the striking TA"

	.globl	unnamed_33
unnamed_33:
	.string	"MR"

	.globl	unnamed_34
unnamed_34:
	.string	"Mars"

	.globl	unnamed_35
unnamed_35:
	.string	"Mars"

	.text

	.globl	array.size
array.size:
.b_0:
	mv	s0, s0
	mv	s1, s1
	mv	s2, s2
	mv	s3, s3
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	s10, s10
	mv	s11, s11
	mv	ra, ra
	mv	a0, a0
	lw	a0, 0(a0)
	mv	a0, a0
	mv	s3, s3
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	ra, ra
	mv	s11, s11
	mv	s10, s10
	mv	s0, s0
	mv	s1, s1
	mv	s2, s2
	ret

	.globl	main
main:
.b_1:
	addi	sp, sp, -16
	mv	s0, s0
	sw	s0, 12(sp)
	mv	s1, s1
	mv	s2, s2
	mv	s3, s3
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	s10, s10
	mv	s11, s11
	mv	s0, ra
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	t0, 100
	sw	t0, init_anger, ra
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	t0, 10
	sw	t0, work_anger, ra
	call	_main
	mv	a0, a0
	mv	a0, a0
	mv	s3, s3
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	ra, s0
	mv	s11, s11
	mv	s10, s10
	lw	s0, 12(sp)
	mv	s0, s0
	mv	s1, s1
	mv	s2, s2
	addi	sp, sp, 16
	ret

	.globl	work
work:
.entry_2:
	addi	sp, sp, -16
	mv	s0, s0
	sw	s0, 8(sp)
	mv	s1, s1
	sw	s1, 12(sp)
	mv	s2, s2
	sw	s2, 4(sp)
	mv	s2, s3
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	s10, s10
	mv	s11, s11
	mv	s0, ra
	mv	a0, a0
	mv	s1, a1
	addi	ra, s1, 4
	lw	t0, 0(ra)
	li	ra, 100
	slt	ra, ra, t0
	xori	ra, ra, 1
	bne	ra, zero, .if_then_3
	j	.if_else_4
.if_then_3:
	mv	a0, a0
	la	a1, unnamed_5
	mv	a1, a1
	call	string_add
	mv	a0, a0
	addi	ra, s1, 0
	lw	a1, 0(ra)
	mv	a0, a0
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	a0, a0
	la	a1, unnamed_10
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	a0, a0
	call	println
	j	.if_exit_5
.if_exit_5:
	addi	t1, s1, 4
	addi	ra, s1, 4
	lw	t0, 0(ra)
	lw	ra, work_anger
	add	ra, t0, ra
	sw	ra, 0(t1)
	mv	s3, s2
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	ra, s0
	mv	s11, s11
	mv	s10, s10
	lw	s0, 8(sp)
	mv	s0, s0
	lw	s1, 12(sp)
	mv	s1, s1
	lw	s2, 4(sp)
	mv	s2, s2
	addi	sp, sp, 16
	ret
.if_else_4:
	mv	a0, a0
	la	a1, unnamed_12
	mv	a1, a1
	call	string_add
	mv	a0, a0
	addi	ra, s1, 0
	lw	a1, 0(ra)
	mv	a0, a0
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	a0, a0
	la	a1, unnamed_17
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	a0, a0
	call	println
	j	.if_exit_5

	.globl	_main
_main:
.entry_6:
	addi	sp, sp, -16
	mv	s0, s0
	sw	s0, 4(sp)
	mv	s1, s1
	sw	s1, 8(sp)
	mv	s2, s2
	sw	s2, 12(sp)
	mv	s3, s3
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	s10, s10
	mv	s11, s11
	mv	s0, ra
	li	a0, 8
	mv	a0, a0
	call	malloc
	mv	a0, a0
	mv	s2, a0
	addi	t0, s2, 0
	la	ra, unnamed_26
	sw	ra, 0(t0)
	addi	ra, s2, 4
	sw	zero, 0(ra)
	li	a0, 8
	mv	a0, a0
	call	malloc
	mv	a0, a0
	mv	s1, a0
	addi	t0, s1, 0
	la	ra, unnamed_30
	sw	ra, 0(t0)
	addi	t0, s1, 4
	lw	ra, init_anger
	sw	ra, 0(t0)
	la	a0, unnamed_33
	mv	a0, a0
	mv	a1, s2
	call	work
	la	a0, unnamed_34
	mv	a0, a0
	mv	a1, s1
	call	work
	la	a0, unnamed_35
	mv	a0, a0
	mv	a1, s1
	call	work
	mv	a0, zero
	mv	s3, s3
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	ra, s0
	mv	s11, s11
	mv	s10, s10
	lw	s0, 4(sp)
	mv	s0, s0
	lw	s1, 8(sp)
	mv	s1, s1
	lw	s2, 12(sp)
	mv	s2, s2
	addi	sp, sp, 16
	ret

