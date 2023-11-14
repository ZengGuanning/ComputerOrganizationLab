package lab3
import chisel3._

class RegFileInterface extends Bundle {
    val wen = Input(Bool()) 
    val waddr = Input(UInt(5.W))
    val wdata = Input(UInt(16.W))
    val raddr1 = Input(UInt(5.W))
    val raddr2 = Input(UInt(5.W))
    val rdata1 = Output(UInt(16.W))
    val rdata2 = Output(UInt(16.W))
}

class RegFile extends Module {
    
    val io = IO(new RegFileInterface)

    val registers = RegInit(VecInit(Seq.fill(32)(0.U(16.W)))) // 32个16位寄存器 初始化为0

    when(io.wen === true.B && io.waddr =/= 0.U) {
        registers(io.waddr) := io.wdata
    }

    io.rdata1 := registers(io.raddr1)
    io.rdata2 := registers(io.raddr2)

}