package lab3
import chisel3._
import chisel3.util._

class ALUInterface extends Bundle {
    val a = Input(UInt(16.W))
    val b = Input(UInt(16.W))
    val op = Input(UInt(4.W))
    val y = Output(UInt(16.W))
}

class ALU extends Module {
    val io = IO(new ALUInterface)
    
    io.y := MuxCase(0.U,
        Array(
            (io.op === 1.U) -> (io.a + io.b),
            (io.op === 2.U) -> (io.a - io.b),
            (io.op === 3.U) -> (io.a & io.b),
            (io.op === 4.U) -> (io.a | io.b),
            (io.op === 5.U) -> (io.a ^ io.b),
            (io.op === 6.U) -> (~io.a),
            // a 逻辑左移 b 位
            (io.op === 7.U) -> (io.a << io.b(3, 0)),
            // a 逻辑右移 b 位
            (io.op === 8.U) -> (io.a >> io.b(3, 0)),
            // a 算术右移 b 位
            (io.op === 9.U) -> (io.a.asSInt >> io.b(3, 0)).asUInt,
            // a 循环左移 b 位
            (io.op === 10.U) -> ((io.a << io.b(3, 0)) | (io.a >> (16.U - io.b(3, 0)))),
        )
    )
}