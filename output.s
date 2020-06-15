	.section	.data

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

	.globl	_main
_main:
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
	li	t1, 10
	mv	t1, t1
	li	a0, 0
	mv	a0, a0
	li	t0, 1
	mv	t0, t0
	j	.for_cond_3
.for_cond_3:
	slt	t2, t1, t0
	xori	t2, t2, 1
	bne	t2, zero, .for_loop_4
	j	.for_exit_5
.for_exit_5:
	li	t0, 1
	mv	t0, t0
	j	.for_cond_6
.for_cond_6:
	slt	t2, t1, t0
	xori	t2, t2, 1
	bne	t2, zero, .for_loop_7
	j	.for_exit_8
.for_exit_8:
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
.for_loop_7:
	addi	t2, a0, 10
	add	a0, t2, t0
	mv	a0, a0
	j	.for_step_9
.for_step_9:
	addi	t0, t0, 1
	j	.for_cond_6
.for_loop_4:
	add	a0, a0, t0
	mv	a0, a0
	j	.for_step_10
.for_step_10:
	addi	t0, t0, 1
	j	.for_cond_3

