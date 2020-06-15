	.section	.data

	.globl	unnamed_100
unnamed_100:
	.string	"( "

	.globl	unnamed_113
unnamed_113:
	.string	", "

	.globl	unnamed_123
unnamed_123:
	.string	" )"

	.globl	unnamed_157
unnamed_157:
	.string	"vector x: "

	.globl	unnamed_162
unnamed_162:
	.string	"excited!"

	.globl	unnamed_163
unnamed_163:
	.string	"vector y: "

	.globl	unnamed_165
unnamed_165:
	.string	"x + y: "

	.globl	unnamed_168
unnamed_168:
	.string	"x * y: "

	.globl	unnamed_171
unnamed_171:
	.string	"(1 << 3) * y: "

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

	.globl	vector_init
vector_init:
.entry_2:
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
	mv	s3, a0
	mv	s2, a1
	sub	ra, s2, zero
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_3
	j	.if_exit_4
.if_then_3:
	j	.func_exit_5
.func_exit_5:
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
.if_exit_4:
	addi	s4, s3, 0
	mv	a0, s2
	call	array.size
	mv	s1, a0
	li	ra, 4
	mul	ra, s1, ra
	addi	a0, ra, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	sw	s1, 0(a0)
	sw	a0, 0(s4)
	mv	s1, zero
	j	.for_cond_6
.for_cond_6:
	mv	a0, s2
	call	array.size
	mv	a0, a0
	slt	ra, s1, a0
	bne	ra, zero, .for_loop_7
	j	.for_exit_8
.for_exit_8:
	j	.func_exit_5
.for_loop_7:
	addi	ra, s3, 0
	lw	t1, 0(ra)
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t1, t1, ra
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, s2, ra
	lw	ra, 0(ra)
	sw	ra, 0(t1)
	j	.for_step_9
.for_step_9:
	addi	s1, s1, 1
	j	.for_cond_6

	.globl	vector_getDim
vector_getDim:
.entry_10:
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
	mv	a0, a0
	addi	ra, a0, 0
	lw	ra, 0(ra)
	sub	ra, ra, zero
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_11
	j	.if_exit_12
.if_then_11:
	mv	zero, zero
	j	.func_exit_13
.func_exit_13:
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
.if_exit_12:
	addi	ra, a0, 0
	lw	a0, 0(ra)
	mv	a0, a0
	call	array.size
	mv	zero, a0
	mv	zero, zero
	j	.func_exit_13

	.globl	vector_dot
vector_dot:
.entry_14:
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
	mv	s3, a0
	mv	s1, a1
	mv	s4, zero
	mv	s2, zero
	j	.while_cond_15
.while_cond_15:
	mv	a0, s3
	call	vector_getDim
	mv	a0, a0
	slt	ra, s4, a0
	bne	ra, zero, .while_loop_16
	j	.while_exit_17
.while_loop_16:
	addi	ra, s3, 0
	lw	t1, 0(ra)
	addi	t0, s4, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t2, t1, ra
	addi	ra, s1, 0
	lw	t1, 0(ra)
	addi	t0, s4, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	t0, 0(t2)
	lw	ra, 0(ra)
	mul	ra, t0, ra
	mv	s2, ra
	addi	s4, s4, 1
	j	.while_cond_15
.while_exit_17:
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

	.globl	vector_scalarInPlaceMultiply
vector_scalarInPlaceMultiply:
.entry_18:
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
	mv	s3, a0
	mv	s2, a1
	addi	ra, s3, 0
	lw	ra, 0(ra)
	sub	ra, ra, zero
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_19
	j	.if_exit_20
.if_then_19:
	mv	zero, zero
	j	.func_exit_21
.func_exit_21:
	mv	a0, zero
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
.if_exit_20:
	mv	s1, zero
	j	.for_cond_22
.for_cond_22:
	mv	a0, s3
	call	vector_getDim
	mv	a0, a0
	slt	ra, s1, a0
	bne	ra, zero, .for_loop_23
	j	.for_exit_24
.for_loop_23:
	addi	ra, s3, 0
	lw	t1, 0(ra)
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t2, t1, ra
	addi	ra, s3, 0
	lw	t1, 0(ra)
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	mul	ra, s2, ra
	sw	ra, 0(t2)
	j	.for_step_25
.for_step_25:
	addi	s1, s1, 1
	j	.for_cond_22
.for_exit_24:
	mv	zero, s3
	j	.func_exit_21

	.globl	vector_add
vector_add:
.entry_26:
	addi	sp, sp, -32
	mv	s0, s0
	sw	s0, 8(sp)
	mv	s1, s1
	sw	s1, 12(sp)
	mv	s2, s2
	sw	s2, 16(sp)
	mv	s3, s3
	sw	s3, 20(sp)
	mv	s4, s4
	sw	s4, 24(sp)
	mv	s5, s5
	sw	s5, 28(sp)
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
	call	vector_getDim
	mv	s3, a0
	mv	a0, s2
	call	vector_getDim
	mv	a0, a0
	sub	ra, s3, a0
	sltu	ra, zero, ra
	bne	ra, zero, .or_true_27
	j	.or_false_28
.or_false_28:
	mv	a0, s1
	call	vector_getDim
	mv	a0, a0
	sub	ra, a0, zero
	sltiu	ra, ra, 1
	mv	ra, ra
	j	.or_exit_29
.or_exit_29:
	bne	ra, zero, .if_then_30
	j	.if_exit_31
.if_exit_31:
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	mv	s3, a0
	addi	s4, s3, 0
	mv	a0, s1
	call	vector_getDim
	mv	s5, a0
	li	ra, 4
	mul	ra, s5, ra
	addi	a0, ra, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	sw	s5, 0(a0)
	sw	a0, 0(s4)
	mv	s4, zero
	j	.for_cond_32
.for_cond_32:
	mv	a0, s1
	call	vector_getDim
	mv	a0, a0
	slt	ra, s4, a0
	bne	ra, zero, .for_loop_33
	j	.for_exit_34
.for_loop_33:
	addi	ra, s3, 0
	lw	t1, 0(ra)
	addi	t0, s4, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	addi	t0, s1, 0
	lw	t2, 0(t0)
	addi	t1, s4, 1
	li	t0, 4
	mul	t0, t1, t0
	add	a0, t2, t0
	addi	t0, s2, 0
	lw	t2, 0(t0)
	addi	t1, s4, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t0, t2, t0
	lw	t1, 0(a0)
	lw	t0, 0(t0)
	add	t0, t1, t0
	sw	t0, 0(ra)
	j	.for_step_35
.for_step_35:
	addi	s4, s4, 1
	j	.for_cond_32
.for_exit_34:
	mv	a0, s3
	j	.func_exit_36
.func_exit_36:
	mv	a0, a0
	lw	s3, 20(sp)
	mv	s3, s3
	lw	s4, 24(sp)
	mv	s4, s4
	lw	s5, 28(sp)
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
	lw	s2, 16(sp)
	mv	s2, s2
	addi	sp, sp, 32
	ret
.if_then_30:
	mv	a0, zero
	j	.func_exit_36
.or_true_27:
	li	ra, 1
	mv	ra, ra
	j	.or_exit_29

	.globl	vector_set
vector_set:
.entry_37:
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
	mv	s2, a0
	mv	s3, a1
	mv	s1, a2
	mv	a0, s2
	call	vector_getDim
	mv	a0, a0
	slt	ra, a0, s3
	bne	ra, zero, .if_then_38
	j	.if_exit_39
.if_then_38:
	mv	zero, zero
	j	.func_exit_40
.func_exit_40:
	mv	a0, zero
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
.if_exit_39:
	addi	ra, s2, 0
	lw	t1, 0(ra)
	addi	t0, s3, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	sw	s1, 0(ra)
	li	zero, 1
	mv	zero, zero
	j	.func_exit_40

	.globl	vector_tostring
vector_tostring:
.entry_41:
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
	mv	s2, a0
	la	ra, unnamed_100
	mv	s3, ra
	mv	a0, s2
	call	vector_getDim
	mv	a0, a0
	slt	ra, zero, a0
	bne	ra, zero, .if_then_42
	j	.if_exit_43
.if_exit_43:
	li	ra, 1
	mv	s1, ra
	j	.for_cond_44
.for_cond_44:
	mv	a0, s2
	call	vector_getDim
	mv	a0, a0
	slt	ra, s1, a0
	bne	ra, zero, .for_loop_45
	j	.for_exit_46
.for_exit_46:
	mv	a0, s3
	la	a1, unnamed_123
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	s3, a0
	mv	a0, s3
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
.for_loop_45:
	mv	a0, s3
	la	a1, unnamed_113
	mv	a1, a1
	call	string_add
	mv	s3, a0
	addi	ra, s2, 0
	lw	t1, 0(ra)
	addi	t0, s1, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	a0, 0(ra)
	mv	a0, a0
	call	toString
	mv	a1, a0
	mv	a0, s3
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	s3, a0
	j	.for_step_47
.for_step_47:
	addi	s1, s1, 1
	j	.for_cond_44
.if_then_42:
	addi	ra, s2, 0
	lw	t1, 0(ra)
	li	t0, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	a0, 0(ra)
	mv	a0, a0
	call	toString
	mv	a1, a0
	mv	a0, s3
	mv	a1, a1
	call	string_add
	mv	a0, a0
	mv	s3, a0
	j	.if_exit_43

	.globl	vector_copy
vector_copy:
.entry_48:
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
	mv	s3, a1
	sub	ra, s3, zero
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_49
	j	.if_exit_50
.if_then_49:
	mv	zero, zero
	j	.func_exit_51
.func_exit_51:
	mv	a0, zero
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
.if_exit_50:
	mv	a0, s3
	call	vector_getDim
	mv	a0, a0
	sub	ra, a0, zero
	sltiu	ra, ra, 1
	bne	ra, zero, .if_then_52
	j	.if_else_53
.if_else_53:
	addi	s2, s1, 0
	mv	a0, s3
	call	vector_getDim
	mv	s4, a0
	li	ra, 4
	mul	ra, s4, ra
	addi	a0, ra, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	sw	s4, 0(a0)
	sw	a0, 0(s2)
	mv	s2, zero
	j	.for_cond_54
.for_cond_54:
	mv	a0, s1
	call	vector_getDim
	mv	a0, a0
	slt	ra, s2, a0
	bne	ra, zero, .for_loop_55
	j	.for_exit_56
.for_loop_55:
	addi	ra, s1, 0
	lw	t1, 0(ra)
	addi	t0, s2, 1
	li	ra, 4
	mul	ra, t0, ra
	add	t2, t1, ra
	addi	ra, s3, 0
	lw	t1, 0(ra)
	addi	t0, s2, 1
	li	ra, 4
	mul	ra, t0, ra
	add	ra, t1, ra
	lw	ra, 0(ra)
	sw	ra, 0(t2)
	j	.for_step_57
.for_step_57:
	addi	s2, s2, 1
	j	.for_cond_54
.for_exit_56:
	j	.if_exit_58
.if_exit_58:
	li	zero, 1
	mv	zero, zero
	j	.func_exit_51
.if_then_52:
	addi	ra, s1, 0
	sw	zero, 0(ra)
	j	.if_exit_58

	.globl	_main
_main:
.entry_59:
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
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	mv	s1, a0
	li	ra, 40
	addi	a0, ra, 4
	mv	a0, a0
	call	malloc
	mv	a1, a0
	li	ra, 10
	sw	ra, 0(a1)
	mv	a1, a1
	mv	ra, zero
	j	.for_cond_60
.for_cond_60:
	slti	t0, ra, 10
	bne	t0, zero, .for_loop_61
	j	.for_exit_62
.for_loop_61:
	addi	t1, ra, 1
	li	t0, 4
	mul	t0, t1, t0
	add	t1, a1, t0
	li	t0, 9
	sub	t0, t0, ra
	sw	t0, 0(t1)
	j	.for_step_63
.for_step_63:
	addi	ra, ra, 1
	j	.for_cond_60
.for_exit_62:
	mv	a0, s1
	mv	a1, a1
	call	vector_init
	la	a0, unnamed_157
	mv	a0, a0
	call	print
	mv	a0, s1
	call	vector_tostring
	mv	a0, a0
	mv	a0, a0
	call	println
	li	a0, 4
	mv	a0, a0
	call	malloc
	mv	a0, a0
	mv	s2, a0
	mv	a0, s2
	mv	a1, s1
	call	vector_copy
	mv	a0, a0
	mv	a0, s2
	li	a1, 3
	mv	a1, a1
	li	a2, 817
	mv	a2, a2
	call	vector_set
	mv	a0, a0
	bne	a0, zero, .if_then_64
	j	.if_exit_65
.if_exit_65:
	la	a0, unnamed_163
	mv	a0, a0
	call	print
	mv	a0, s2
	call	vector_tostring
	mv	a0, a0
	mv	a0, a0
	call	println
	la	a0, unnamed_165
	mv	a0, a0
	call	print
	mv	a0, s1
	mv	a1, s2
	call	vector_add
	mv	a0, a0
	mv	a0, a0
	call	vector_tostring
	mv	a0, a0
	mv	a0, a0
	call	println
	la	a0, unnamed_168
	mv	a0, a0
	call	print
	mv	a0, s1
	mv	a1, s2
	call	vector_dot
	mv	a0, a0
	mv	a0, a0
	call	toString
	mv	a0, a0
	mv	a0, a0
	call	println
	la	a0, unnamed_171
	mv	a0, a0
	call	print
	li	a1, 8
	mv	a0, s2
	mv	a1, a1
	call	vector_scalarInPlaceMultiply
	mv	a0, a0
	mv	a0, a0
	call	vector_tostring
	mv	a0, a0
	mv	a0, a0
	call	println
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
.if_then_64:
	la	a0, unnamed_162
	mv	a0, a0
	call	println
	j	.if_exit_65

