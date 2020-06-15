	.section	.data

	.globl	n
n:
	.zero	4

	.globl	h
h:
	.zero	4

	.globl	now
now:
	.zero	4

	.globl	a
a:
	.zero	4

	.globl	A
A:
	.zero	4

	.globl	M
M:
	.zero	4

	.globl	Q
Q:
	.zero	4

	.globl	R
R:
	.zero	4

	.globl	seed
seed:
	.zero	4

	.globl	unnamed_53
unnamed_53:
	.string	" "

	.globl	unnamed_55
unnamed_55:
	.string	""

	.globl	unnamed_165
unnamed_165:
	.string	"Sorry, the number n must be a number s.t. there exists i satisfying n=1+2+...+i"

	.globl	unnamed_166
unnamed_166:
	.string	"Let's start!"

	.globl	unnamed_213
unnamed_213:
	.string	"step "

	.globl	unnamed_216
unnamed_216:
	.string	":"

	.globl	unnamed_218
unnamed_218:
	.string	"Total: "

	.globl	unnamed_221
unnamed_221:
	.string	" step(s)"

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
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	t0, 48271
	sw	t0, A, ra
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	t0, 2147483647
	sw	t0, M, ra
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	t0, 1
	sw	t0, seed, ra
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

	.globl	random
random:
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
	lw	t1, seed
	lw	t0, Q
	rem	t1, t1, t0
	lw	t0, A
	mul	t2, t0, t1
	lw	t1, seed
	lw	t0, Q
	div	t1, t1, t0
	lw	t0, R
	mul	t0, t0, t1
	sub	t0, t2, t0
	mv	t0, t0
	slti	t1, t0, 0
	xori	t1, t1, 1
	bne	t1, zero, .if_then_3
	j	.if_else_4
.if_then_3:
	sw	t0, seed, t1
	j	.if_exit_5
.if_exit_5:
	lw	a0, seed
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
.if_else_4:
	lw	t1, M
	add	t1, t0, t1
	sw	t1, seed, t0
	j	.if_exit_5

	.globl	initialize
initialize:
.entry_6:
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
	sw	a0, seed, t0
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

	.globl	swap
swap:
.entry_7:
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
	mv	a1, a1
	lw	t2, a
	addi	t1, a0, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	t0, 0(t0)
	mv	t0, t0
	lw	a2, a
	addi	t2, a0, 1
	li	t1, 4
	mul	t1, t2, t1
	add	a2, a2, t1
	lw	a0, a
	addi	t2, a1, 1
	li	t1, 4
	mul	t1, t2, t1
	add	t1, a0, t1
	lw	t1, 0(t1)
	sw	t1, 0(a2)
	lw	a0, a
	addi	t2, a1, 1
	li	t1, 4
	mul	t1, t2, t1
	add	t1, a0, t1
	sw	t0, 0(t1)
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

	.globl	pd
pd:
.entry_8:
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
	j	.for_cond_9
.for_cond_9:
	lw	t0, h
	slt	t0, a0, t0
	xori	t0, t0, 1
	bne	t0, zero, .for_loop_10
	j	.for_exit_11
.for_loop_10:
	lw	t0, h
	addi	t1, t0, 1
	lw	t0, h
	mul	t1, t0, t1
	li	t0, 2
	div	t0, t1, t0
	sub	t0, a0, t0
	sltiu	t0, t0, 1
	bne	t0, zero, .if_then_12
	j	.if_exit_13
.if_then_12:
	li	a0, 1
	mv	a0, a0
	j	.func_exit_14
.func_exit_14:
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
.if_exit_13:
	j	.for_step_15
.for_step_15:
	lw	t1, h
	addi	t1, t1, 1
	sw	t1, h, t0
	j	.for_cond_9
.for_exit_11:
	mv	a0, zero
	j	.func_exit_14

	.globl	show
show:
.entry_16:
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
	mv	s1, zero
	j	.for_cond_17
.for_cond_17:
	lw	ra, now
	slt	ra, s1, ra
	bne	ra, zero, .for_loop_18
	j	.for_exit_19
.for_exit_19:
	la	a0, unnamed_55
	mv	a0, a0
	call	println
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
.for_loop_18:
	lw	t1, a
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	a0, 0(ra)
	mv	a0, a0
	call	toString
	mv	a0, a0
	mv	a0, a0
	la	a1, unnamed_53
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	a0, a0
	call	print
	j	.for_step_20
.for_step_20:
	addi	s1, s1, 1
	j	.for_cond_17

	.globl	win
win:
.entry_21:
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
	li	ra, 400
	addi	a0, ra, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	ra, 100
	sw	ra, 0(a0)
	mv	a0, a0
	lw	t0, now
	lw	ra, h
	sub	ra, t0, ra
	sltu	ra, zero, ra
	bne	ra, zero, .if_then_22
	j	.if_exit_23
.if_then_22:
	mv	zero, zero
	j	.func_exit_24
.func_exit_24:
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
	lw	s0, 12(sp)
	mv	s0, s0
	mv	s1, s1
	mv	s2, s2
	addi	sp, sp, 16
	ret
.if_exit_23:
	mv	t0, zero
	j	.for_cond_25
.for_cond_25:
	lw	ra, now
	slt	ra, t0, ra
	bne	ra, zero, .for_loop_26
	j	.for_exit_27
.for_loop_26:
	addi	t1, t0, 1
	li	ra, 4
	mul	ra, t1, ra
	add	a1, a0, ra
	lw	t2, a
	addi	t1, t0, 1
	li	ra, 4
	mul	ra, t1, ra
	add	ra, t2, ra
	lw	ra, 0(ra)
	sw	ra, 0(a1)
	j	.for_step_28
.for_step_28:
	addi	t0, t0, 1
	j	.for_cond_25
.for_exit_27:
	mv	t1, zero
	j	.for_cond_29
.for_cond_29:
	lw	t0, now
	li	ra, 1
	sub	ra, t0, ra
	slt	ra, t1, ra
	bne	ra, zero, .for_loop_30
	j	.for_exit_31
.for_exit_31:
	mv	t1, zero
	j	.for_cond_32
.for_cond_32:
	lw	ra, now
	slt	ra, t1, ra
	bne	ra, zero, .for_loop_33
	j	.for_exit_34
.for_loop_33:
	addi	t0, t1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, a0, ra
	addi	t0, t1, 1
	lw	ra, 0(ra)
	sub	ra, ra, t0
	sltu	ra, zero, ra
	bne	ra, zero, .if_then_35
	j	.if_exit_36
.if_exit_36:
	j	.for_step_37
.for_step_37:
	addi	t1, t1, 1
	j	.for_cond_32
.if_then_35:
	mv	zero, zero
	j	.func_exit_24
.for_exit_34:
	li	zero, 1
	mv	zero, zero
	j	.func_exit_24
.for_loop_30:
	addi	t0, t1, 1
	mv	t0, t0
	j	.for_cond_38
.for_cond_38:
	lw	ra, now
	slt	ra, t0, ra
	bne	ra, zero, .for_loop_39
	j	.for_exit_40
.for_loop_39:
	addi	t2, t1, 1
	li	ra, 4
	mul	ra, t2, ra
	add	a1, a0, ra
	addi	t2, t0, 1
	li	ra, 4
	mul	ra, t2, ra
	add	ra, a0, ra
	lw	t2, 0(a1)
	lw	ra, 0(ra)
	slt	ra, ra, t2
	bne	ra, zero, .if_then_41
	j	.if_exit_42
.if_exit_42:
	j	.for_step_43
.for_step_43:
	addi	t0, t0, 1
	j	.for_cond_38
.if_then_41:
	addi	t2, t1, 1
	li	ra, 4
	mul	ra, t2, ra
	add	ra, a0, ra
	lw	ra, 0(ra)
	mv	ra, ra
	addi	a1, t1, 1
	li	t2, 4
	mul	t2, a1, t2
	add	a2, a0, t2
	addi	a1, t0, 1
	li	t2, 4
	mul	t2, a1, t2
	add	t2, a0, t2
	lw	t2, 0(t2)
	sw	t2, 0(a2)
	addi	a1, t0, 1
	li	t2, 4
	mul	t2, a1, t2
	add	t2, a0, t2
	sw	ra, 0(t2)
	j	.if_exit_42
.for_exit_40:
	j	.for_step_44
.for_step_44:
	addi	t1, t1, 1
	j	.for_cond_29

	.globl	merge
merge:
.entry_45:
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
	mv	s1, zero
	j	.for_cond_46
.for_cond_46:
	lw	ra, now
	slt	ra, s1, ra
	bne	ra, zero, .for_loop_47
	j	.for_exit_48
.for_loop_47:
	lw	t1, a
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	sub	ra, ra, zero
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_49
	j	.if_exit_50
.if_exit_50:
	j	.for_step_51
.for_step_51:
	addi	s1, s1, 1
	j	.for_cond_46
.if_then_49:
	addi	ra, s1, 1
	mv	s2, ra
	j	.for_cond_52
.for_cond_52:
	lw	ra, now
	slt	ra, s2, ra
	bne	ra, zero, .for_loop_53
	j	.for_exit_54
.for_loop_53:
	lw	t1, a
	addi	t0, s2, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	sub	ra, ra, zero
	sltu	ra, zero, ra
	bne	ra, zero, .if_then_55
	j	.if_exit_56
.if_then_55:
	mv	a0, s1
	mv	a1, s2
	call	swap
	j	.for_exit_54
	j	.if_exit_56
.if_exit_56:
	j	.for_step_57
.for_step_57:
	addi	s2, s2, 1
	j	.for_cond_52
.for_exit_54:
	j	.if_exit_50
.for_exit_48:
	mv	s1, zero
	j	.for_cond_58
.for_cond_58:
	lw	ra, now
	slt	ra, s1, ra
	bne	ra, zero, .for_loop_59
	j	.for_exit_60
.for_loop_59:
	lw	t1, a
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	sub	ra, ra, zero
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_61
	j	.if_exit_62
.if_exit_62:
	j	.for_step_63
.for_step_63:
	addi	s1, s1, 1
	j	.for_cond_58
.if_then_61:
	sw	s1, now, ra
	j	.for_exit_60
	j	.if_exit_62
.for_exit_60:
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

	.globl	move
move:
.entry_64:
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
	mv	t0, zero
	j	.for_cond_65
.for_cond_65:
	lw	t1, now
	slt	t1, t0, t1
	bne	t1, zero, .for_loop_66
	j	.for_exit_67
.for_loop_66:
	lw	a0, a
	addi	t2, t0, 1
	li	t1, 4
	mul	t1, t2, t1
	add	a0, a0, t1
	lw	t2, 0(a0)
	li	t1, 1
	sub	t2, t2, t1
	sw	t2, 0(a0)
	addi	t0, t0, 1
	mv	t0, t0
	j	.for_step_68
.for_step_68:
	j	.for_cond_65
.for_exit_67:
	lw	t2, a
	lw	t0, now
	addi	t1, t0, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t1, t2, t0
	lw	t0, now
	sw	t0, 0(t1)
	lw	t0, now
	addi	t1, t0, 1
	sw	t1, now, t0
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

	.globl	_main
_main:
.entry_69:
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
	mv	s2, zero
	mv	s1, zero
	mv	s3, zero
	li	t0, 21
	li	ra, 10
	mul	t0, t0, ra
	sw	t0, n, ra
	sw	zero, h, ra
	li	ra, 400
	addi	a0, ra, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	li	ra, 100
	sw	ra, 0(a0)
	sw	a0, a, ra
	lw	t0, M
	lw	ra, A
	div	t0, t0, ra
	sw	t0, Q, ra
	lw	t0, M
	lw	ra, A
	rem	t0, t0, ra
	sw	t0, R, ra
	lw	a0, n
	mv	a0, a0
	call	pd
	mv	a0, a0
	bne	a0, zero, .b_70
	j	.b_71
.b_71:
	li	ra, 1
	mv	ra, ra
	j	.b_72
.b_72:
	bne	ra, zero, .if_then_73
	j	.if_exit_74
.if_then_73:
	la	a0, unnamed_165
	mv	a0, a0
	call	println
	li	a0, 1
	mv	a0, a0
	j	.func_exit_75
.func_exit_75:
	mv	a0, a0
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
.if_exit_74:
	la	a0, unnamed_166
	mv	a0, a0
	call	println
	li	a0, 3654898
	mv	a0, a0
	call	initialize
	call	random
	mv	a0, a0
	li	ra, 10
	rem	ra, a0, ra
	addi	t0, ra, 1
	sw	t0, now, ra
	lw	a0, now
	mv	a0, a0
	call	toString
	mv	a0, a0
	mv	a0, a0
	call	println
	j	.for_cond_76
.for_cond_76:
	lw	t0, now
	li	ra, 1
	sub	ra, t0, ra
	slt	ra, s2, ra
	bne	ra, zero, .for_loop_77
	j	.for_exit_78
.for_exit_78:
	lw	t0, now
	li	ra, 1
	sub	ra, t0, ra
	lw	t1, a
	addi	t0, ra, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t0, t1, ra
	lw	ra, n
	sub	ra, ra, s1
	sw	ra, 0(t0)
	call	show
	call	merge
	j	.while_cond_79
.while_cond_79:
	call	win
	mv	a0, a0
	bne	a0, zero, .b_80
	j	.b_81
.b_81:
	li	ra, 1
	mv	ra, ra
	j	.b_82
.b_82:
	bne	ra, zero, .while_loop_83
	j	.while_exit_84
.while_exit_84:
	mv	a0, s3
	call	toString
	mv	a1, a0
	la	a0, unnamed_218
	mv	a0, a0
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	a0, a0
	la	a1, unnamed_221
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	a0, a0
	call	println
	mv	a0, zero
	j	.func_exit_75
.while_loop_83:
	addi	s3, s3, 1
	mv	a0, s3
	call	toString
	mv	a1, a0
	la	a0, unnamed_213
	mv	a0, a0
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	a0, a0
	la	a1, unnamed_216
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	a0, a0
	call	println
	call	move
	call	merge
	call	show
	j	.while_cond_79
.b_80:
	mv	ra, zero
	j	.b_82
.for_loop_77:
	lw	t1, a
	addi	t0, s2, 1
	li	ra, 4
	mul	ra, t0, ra
	add	s4, t1, ra
	call	random
	mv	a0, a0
	li	ra, 10
	rem	ra, a0, ra
	addi	ra, ra, 1
	sw	ra, 0(s4)
	j	.while_cond_85
.while_cond_85:
	lw	t1, a
	addi	t0, s2, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	add	t0, ra, s1
	lw	ra, n
	slt	ra, ra, t0
	bne	ra, zero, .while_loop_86
	j	.while_exit_87
.while_loop_86:
	lw	t1, a
	addi	t0, s2, 1
	li	ra, 4
	mul	ra, t0, ra
	add	s4, t1, ra
	call	random
	mv	a0, a0
	li	ra, 10
	rem	ra, a0, ra
	addi	ra, ra, 1
	sw	ra, 0(s4)
	j	.while_cond_85
.while_exit_87:
	lw	t1, a
	addi	t0, s2, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	add	ra, s1, ra
	mv	s1, ra
	j	.for_step_88
.for_step_88:
	addi	s2, s2, 1
	j	.for_cond_76
.b_70:
	mv	ra, zero
	j	.b_72

