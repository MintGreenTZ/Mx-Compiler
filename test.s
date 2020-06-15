	.section	.data

	.globl	n
n:
	.zero	4

	.globl	a
a:
	.zero	4

	.globl	i
i:
	.zero	4

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
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	ra, 80
	addi	a0, ra, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	ra, 20
	sw	ra, 0(a0)
	sw	a0, a, ra
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
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

	.globl	jud
jud:
.entry_2:
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
	mv	t2, zero
	j	.for_cond_3
.for_cond_3:
	lw	t0, n
	div	t0, t0, a0
	slt	t0, t2, t0
	bne	t0, zero, .for_loop_4
	j	.for_exit_5
.for_exit_5:
	mv	zero, zero
	j	.func_exit_6
.func_exit_6:
	mv	a0, zero
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
.for_loop_4:
	mv	t1, zero
	mv	t0, zero
	j	.for_cond_7
.for_cond_7:
	li	a1, 1
	sub	a1, a0, a1
	slt	a1, t0, a1
	bne	a1, zero, .for_loop_8
	j	.for_exit_9
.for_exit_9:
	bne	t1, zero, .b_10
	j	.b_11
.b_10:
	mv	t0, zero
	j	.b_12
.b_12:
	bne	t0, zero, .if_then_13
	j	.if_exit_14
.if_then_13:
	li	zero, 1
	mv	zero, zero
	j	.func_exit_6
.if_exit_14:
	j	.for_step_15
.for_step_15:
	mv	t2, t2
	addi	t2, t2, 1
	j	.for_cond_3
.b_11:
	li	t0, 1
	mv	t0, t0
	j	.b_12
.for_loop_8:
	mul	a1, t2, a0
	add	a1, a1, t0
	lw	a3, a
	addi	a2, a1, 1
	li	a1, 4
	mul	a1, a2, a1
	add	a4, a3, a1
	mul	a1, t2, a0
	add	a1, a1, t0
	addi	a1, a1, 1
	lw	a3, a
	addi	a2, a1, 1
	li	a1, 4
	mul	a1, a2, a1
	add	a1, a3, a1
	lw	a2, 0(a4)
	lw	a1, 0(a1)
	slt	a1, a1, a2
	bne	a1, zero, .if_then_16
	j	.if_exit_17
.if_then_16:
	li	t1, 1
	mv	t1, t1
	j	.if_exit_17
.if_exit_17:
	j	.for_step_18
.for_step_18:
	mv	t0, t0
	addi	t0, t0, 1
	j	.for_cond_7

	.globl	_main
_main:
.entry_19:
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
	call	getInt
	mv	a0, a0
	sw	a0, n, ra
	sw	zero, i, ra
	j	.for_cond_20
.for_cond_20:
	lw	t0, i
	lw	ra, n
	slt	ra, t0, ra
	bne	ra, zero, .for_loop_21
	j	.for_exit_22
.for_loop_21:
	lw	t1, a
	lw	ra, i
	addi	t0, ra, 1
	li	ra, 4
	mul	ra, t0, ra
	add	s1, t1, ra
	call	getInt
	mv	a0, a0
	sw	a0, 0(s1)
	j	.for_step_23
.for_step_23:
	lw	ra, i
	addi	t0, ra, 1
	sw	t0, i, ra
	j	.for_cond_20
.for_exit_22:
	lw	t0, n
	sw	t0, i, ra
	j	.for_cond_24
.for_cond_24:
	lw	ra, i
	slt	ra, zero, ra
	bne	ra, zero, .for_loop_25
	j	.for_exit_26
.for_exit_26:
	mv	zero, zero
	j	.func_exit_27
.func_exit_27:
	mv	a0, zero
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
.for_loop_25:
	lw	a0, i
	mv	a0, a0
	call	jud
	mv	a0, a0
	slt	ra, zero, a0
	bne	ra, zero, .if_then_28
	j	.if_exit_29
.if_exit_29:
	j	.for_step_30
.for_step_30:
	lw	t0, i
	li	ra, 2
	div	t0, t0, ra
	sw	t0, i, ra
	j	.for_cond_24
.if_then_28:
	lw	a0, i
	mv	a0, a0
	call	toString
	mv	a0, a0
	mv	a0, a0
	call	print
	mv	zero, zero
	j	.func_exit_27

