	.section	.data

	.globl	unnamed_33
unnamed_33:
	.string	"Warning: Queue_int::pop: empty queue"

	.globl	unnamed_98
unnamed_98:
	.string	"q.size() != N after pushes"

	.globl	unnamed_102
unnamed_102:
	.string	"Head != i"

	.globl	unnamed_105
unnamed_105:
	.string	"Failed: q.pop() != i"

	.globl	unnamed_110
unnamed_110:
	.string	"q.size() != N - i - 1"

	.globl	unnamed_111
unnamed_111:
	.string	"Passed tests."

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

	.globl	Queue_int_push
Queue_int_push:
.entry_2:
	addi	sp, sp, -16
	mv	s0, s0
	sw	s0, 0(sp)
	mv	s1, s1
	sw	s1, 4(sp)
	mv	s2, s2
	sw	s2, 8(sp)
	mv	s3, s3
	sw	s3, 12(sp)
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	s10, s10
	mv	s11, s11
	mv	s0, ra
	mv	s1, a0
	mv	s2, a1
	mv	a0, s1
	call	Queue_int_size
	mv	s3, a0
	addi	ra, s1, 0
	lw	a0, 0(ra)
	mv	a0, a0
	call	array.size
	mv	a0, a0
	li	ra, 1
	sub	ra, a0, ra
	sub	ra, s3, ra
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_3
	j	.if_exit_4
.if_then_3:
	mv	a0, s1
	call	Queue_int_doubleStorage
	j	.if_exit_4
.if_exit_4:
	addi	t0, s1, 0
	addi	ra, s1, 8
	lw	t1, 0(t0)
	lw	ra, 0(ra)
	addi	t0, ra, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	sw	s2, 0(ra)
	addi	s2, s1, 8
	addi	ra, s1, 8
	lw	ra, 0(ra)
	addi	s3, ra, 1
	addi	ra, s1, 0
	lw	a0, 0(ra)
	mv	a0, a0
	call	array.size
	mv	a0, a0
	rem	ra, s3, a0
	sw	ra, 0(s2)
	lw	s3, 12(sp)
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
	lw	s0, 0(sp)
	mv	s0, s0
	lw	s1, 4(sp)
	mv	s1, s1
	lw	s2, 8(sp)
	mv	s2, s2
	addi	sp, sp, 16
	ret

	.globl	Queue_int_top
Queue_int_top:
.entry_5:
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
	addi	t1, a0, 0
	addi	t0, a0, 4
	lw	t2, 0(t1)
	lw	t0, 0(t0)
	addi	t1, t0, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	a0, 0(t0)
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

	.globl	Queue_int_pop
Queue_int_pop:
.entry_6:
	addi	sp, sp, -32
	mv	s0, s0
	sw	s0, 12(sp)
	mv	s1, s1
	sw	s1, 16(sp)
	mv	s2, s2
	sw	s2, 20(sp)
	mv	s3, s3
	sw	s3, 24(sp)
	mv	s4, s4
	sw	s4, 28(sp)
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	s10, s10
	mv	s11, s11
	mv	s0, ra
	mv	s1, a0
	mv	a0, s1
	call	Queue_int_size
	mv	a0, a0
	sub	ra, a0, zero
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_7
	j	.if_exit_8
.if_then_7:
	la	a0, unnamed_33
	mv	a0, a0
	call	println
	j	.if_exit_8
.if_exit_8:
	mv	a0, s1
	call	Queue_int_top
	mv	a0, a0
	mv	s2, a0
	addi	s3, s1, 4
	addi	ra, s1, 4
	lw	ra, 0(ra)
	addi	s4, ra, 1
	addi	ra, s1, 0
	lw	a0, 0(ra)
	mv	a0, a0
	call	array.size
	mv	a0, a0
	rem	ra, s4, a0
	sw	ra, 0(s3)
	mv	a0, s2
	lw	s3, 24(sp)
	mv	s3, s3
	lw	s4, 28(sp)
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
	lw	s1, 16(sp)
	mv	s1, s1
	lw	s2, 20(sp)
	mv	s2, s2
	addi	sp, sp, 32
	ret

	.globl	Queue_int_size
Queue_int_size:
.entry_9:
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
	mv	s1, a0
	addi	s2, s1, 8
	addi	ra, s1, 0
	lw	a0, 0(ra)
	mv	a0, a0
	call	array.size
	mv	a0, a0
	lw	ra, 0(s2)
	add	t0, ra, a0
	addi	ra, s1, 4
	lw	ra, 0(ra)
	sub	s2, t0, ra
	addi	ra, s1, 0
	lw	a0, 0(ra)
	mv	a0, a0
	call	array.size
	mv	a0, a0
	rem	a0, s2, a0
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
	lw	s0, 4(sp)
	mv	s0, s0
	lw	s1, 8(sp)
	mv	s1, s1
	lw	s2, 12(sp)
	mv	s2, s2
	addi	sp, sp, 16
	ret

	.globl	Queue_int_doubleStorage
Queue_int_doubleStorage:
.entry_10:
	addi	sp, sp, -32
	mv	s0, s0
	sw	s0, 4(sp)
	mv	s1, s1
	sw	s1, 8(sp)
	mv	s2, s2
	sw	s2, 12(sp)
	mv	s3, s3
	sw	s3, 16(sp)
	mv	s4, s4
	sw	s4, 20(sp)
	mv	s5, s5
	sw	s5, 24(sp)
	mv	s6, s6
	sw	s6, 28(sp)
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	s10, s10
	mv	s11, s11
	mv	s0, ra
	mv	s1, a0
	addi	ra, s1, 0
	lw	ra, 0(ra)
	mv	s5, ra
	addi	ra, s1, 4
	lw	ra, 0(ra)
	mv	s2, ra
	addi	ra, s1, 8
	lw	ra, 0(ra)
	mv	s4, ra
	addi	s3, s1, 0
	mv	a0, s5
	call	array.size
	mv	a0, a0
	li	ra, 2
	mul	s6, a0, ra
	li	ra, 4
	mul	ra, s6, ra
	addi	a0, ra, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	sw	s6, 0(a0)
	sw	a0, 0(s3)
	addi	ra, s1, 4
	sw	zero, 0(ra)
	addi	ra, s1, 8
	sw	zero, 0(ra)
	mv	ra, s2
	j	.while_cond_11
.while_cond_11:
	sub	t0, ra, s4
	sltu	t0, zero, t0
	bne	t0, zero, .while_loop_12
	j	.while_exit_13
.while_exit_13:
	lw	s3, 16(sp)
	mv	s3, s3
	lw	s4, 20(sp)
	mv	s4, s4
	lw	s5, 24(sp)
	mv	s5, s5
	lw	s6, 28(sp)
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
	addi	sp, sp, 32
	ret
.while_loop_12:
	addi	t1, s1, 0
	addi	t0, s1, 8
	lw	t2, 0(t1)
	lw	t0, 0(t0)
	addi	t1, t0, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t2, t2, t0
	addi	t1, ra, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t0, s5, t0
	lw	t0, 0(t0)
	sw	t0, 0(t2)
	addi	t1, s1, 8
	lw	t0, 0(t1)
	addi	t0, t0, 1
	sw	t0, 0(t1)
	addi	s2, ra, 1
	mv	a0, s5
	call	array.size
	mv	a0, a0
	rem	ra, s2, a0
	mv	ra, ra
	j	.while_cond_11

	.globl	Queue_int_constructor
Queue_int_constructor:
.entry_14:
	addi	sp, sp, -16
	mv	s0, s0
	sw	s0, 8(sp)
	mv	s1, s1
	sw	s1, 12(sp)
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
	mv	a0, a0
	addi	ra, a0, 4
	sw	zero, 0(ra)
	addi	ra, a0, 8
	sw	zero, 0(ra)
	addi	s1, a0, 0
	li	ra, 64
	addi	a0, ra, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	ra, 16
	sw	ra, 0(a0)
	sw	a0, 0(s1)
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
	lw	s0, 8(sp)
	mv	s0, s0
	lw	s1, 12(sp)
	mv	s1, s1
	mv	s2, s2
	addi	sp, sp, 16
	ret

	.globl	_main
_main:
.entry_15:
	addi	sp, sp, -16
	mv	s0, s0
	sw	s0, 0(sp)
	mv	s1, s1
	sw	s1, 4(sp)
	mv	s2, s2
	sw	s2, 8(sp)
	mv	s3, s3
	sw	s3, 12(sp)
	mv	s4, s4
	mv	s5, s5
	mv	s6, s6
	mv	s7, s7
	mv	s8, s8
	mv	s9, s9
	mv	s10, s10
	mv	s11, s11
	mv	s0, ra
	li	a0, 12
	mv	a0, a0
	call	malloc
	mv	s1, a0
	mv	a0, s1
	call	Queue_int_constructor
	mv	s1, s1
	li	ra, 100
	mv	s3, ra
	mv	s2, zero
	j	.for_cond_16
.for_cond_16:
	slt	ra, s2, s3
	bne	ra, zero, .for_loop_17
	j	.for_exit_18
.for_exit_18:
	mv	a0, s1
	call	Queue_int_size
	mv	a0, a0
	sub	ra, a0, s3
	sltu	ra, zero, ra
	bne	ra, zero, .if_then_19
	j	.if_exit_20
.if_exit_20:
	mv	s2, zero
	j	.for_cond_21
.for_cond_21:
	slt	ra, s2, s3
	bne	ra, zero, .for_loop_22
	j	.for_exit_23
.for_loop_22:
	mv	a0, s1
	call	Queue_int_top
	mv	a0, a0
	mv	a0, a0
	sub	ra, a0, s2
	sltu	ra, zero, ra
	bne	ra, zero, .if_then_24
	j	.if_exit_25
.if_exit_25:
	mv	a0, s1
	call	Queue_int_pop
	mv	a0, a0
	sub	ra, a0, s2
	sltu	ra, zero, ra
	bne	ra, zero, .if_then_26
	j	.if_exit_27
.if_exit_27:
	mv	a0, s1
	call	Queue_int_size
	mv	a0, a0
	sub	t0, s3, s2
	li	ra, 1
	sub	ra, t0, ra
	sub	ra, a0, ra
	sltu	ra, zero, ra
	bne	ra, zero, .if_then_28
	j	.if_exit_29
.if_then_28:
	la	a0, unnamed_110
	mv	a0, a0
	call	println
	li	a0, 1
	mv	a0, a0
	j	.func_exit_30
.func_exit_30:
	mv	a0, a0
	lw	s3, 12(sp)
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
	lw	s0, 0(sp)
	mv	s0, s0
	lw	s1, 4(sp)
	mv	s1, s1
	lw	s2, 8(sp)
	mv	s2, s2
	addi	sp, sp, 16
	ret
.if_exit_29:
	j	.for_step_31
.for_step_31:
	addi	s2, s2, 1
	j	.for_cond_21
.if_then_26:
	la	a0, unnamed_105
	mv	a0, a0
	call	println
	li	a0, 1
	mv	a0, a0
	j	.func_exit_30
.if_then_24:
	la	a0, unnamed_102
	mv	a0, a0
	call	println
	li	a0, 1
	mv	a0, a0
	j	.func_exit_30
.for_exit_23:
	la	a0, unnamed_111
	mv	a0, a0
	call	println
	mv	a0, zero
	j	.func_exit_30
.if_then_19:
	la	a0, unnamed_98
	mv	a0, a0
	call	println
	li	a0, 1
	mv	a0, a0
	j	.func_exit_30
.for_loop_17:
	mv	a0, s1
	mv	a1, s2
	call	Queue_int_push
	j	.for_step_32
.for_step_32:
	addi	s2, s2, 1
	j	.for_cond_16

