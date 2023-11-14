package lab3
import chisel3._
import chisel3.util._

class Controller extends Module {
    val io = IO(new Bundle{
        val step = Input(Bool())
        val leds = Output(UInt(16.W))
        val inst = Input(UInt(32.W))
    })
    val io_rf = IO(Flipped(new RegFileInterface))
    val io_alu = IO(Flipped(new ALUInterface))

    // 组合逻辑 解析指令中的常用部分
    val is_rtype = io.inst(2, 0) === "b001".U
    val is_itype = io.inst(2, 0) === "b010".U
    val is_peek = io.inst(6, 3) === "b0010".U
    val is_poke = io.inst(6, 3) === "b0001".U
    val imm = Wire(UInt(16.W))
    val rd, rs1, rs2 = Wire(UInt(5.W))
    val alu_op = Wire(UInt(4.W))

    // is_rtype := io.inst(2, 0) === "b001".U
    // is_itype := io.inst(2, 0) === "b010".U
    // is_peek := io.inst(6, 3) === "b0010".U
    // is_poke := io.inst(6, 3) === "b0001".U
    imm := io.inst(31, 16)
    rd := io.inst(11, 7)
    rs1 := io.inst(19, 15)
    rs2 := io.inst(24, 20)
    alu_op := io.inst(6, 3)

    // 状态机
    val s_idle :: s_decode :: Nil = Enum(2)
    
    val state = RegInit(s_idle)

    // 为io_rf设置默认值
    io_rf.wen := false.B
    io_rf.waddr := 0.U
    io_rf.wdata := 0.U
    io_rf.raddr1 := 0.U
    io_rf.raddr2 := 0.U
    // 为io_alu设置默认值
    io_alu.a := 0.U
    io_alu.b := 0.U
    io_alu.op := 0.U


    val leds_reg = RegInit(0.U(16.W))

    io.leds := leds_reg

    when(state === s_idle){
        when(io.step){
            state := s_decode
            io_rf.wen := false.B
        }
    }.elsewhen(state === s_decode){
        when(is_rtype){
            state := s_idle
            io_rf.raddr1 := rs1
            io_rf.raddr2 := rs2
            io_alu.a := io_rf.rdata1
            io_alu.b := io_rf.rdata2
            io_alu.op := alu_op
            io_rf.wen := true.B
            io_rf.wdata := io_alu.y
            io_rf.waddr := rd
        }.elsewhen(is_itype & is_peek){
            state := s_idle
            io_rf.raddr1 := rd
            leds_reg := io_rf.rdata1
        }.elsewhen(is_itype & is_poke){
            state := s_idle
            io_rf.wen := true.B
            io_rf.waddr := rd
            io_rf.wdata := imm
        }.otherwise{
            state := s_idle
        }
    }.otherwise{
        state := s_idle
    }
}

