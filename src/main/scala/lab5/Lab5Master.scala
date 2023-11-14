package lab5
import chisel3._
import chisel3.util._
class Lab5Master (
    ADDR_WIDTH: Int = 32,
    DATA_WIDTH: Int = 32
) extends Module {
    // 控制信号
    val io = IO(new Bundle{
        val dip_sw = Input(UInt(32.W))
    })

    // wishbone master
    val io_wb = IO(new Bundle{
        val cyc_o = Output(Bool())
        val stb_o = Output(Bool())
        val ack_i = Input(Bool())
        val adr_o = Output(UInt(ADDR_WIDTH.W))
        val dat_o = Output(UInt(DATA_WIDTH.W))
        val dat_i = Input(UInt(DATA_WIDTH.W))
        val sel_o = Output(UInt((DATA_WIDTH/8).W))
        val we_o = Output(Bool())
    })

    val List(
        read_wait_prepare,
        read_wait_action,
        read_wait_check,
        read_data_action,
        read_data_done,
        write_sram_prepare,
        write_sram_action,
        write_sram_done,
        write_wait_prepare,
        write_wait_action,
        write_wait_check,
        write_data_action,
        write_data_done
    ) = Enum(13)
    val state = RegInit(read_wait_prepare)
    // val stage = RegInit(0.U(2.W)) // 0:idle 1:读串口 2:写SRAM 3:写串口
    val addr = RegInit(0.U(32.W))
    io_wb.adr_o := addr
    val round = RegInit(0.U(6.W))
    val cyc = RegInit(false.B)
    val stb = RegInit(false.B)
    io_wb.cyc_o := cyc
    io_wb.stb_o := stb
    val sel = RegInit(0.U((DATA_WIDTH/8).W))
    io_wb.sel_o := sel
    val we = RegInit(true.B)
    io_wb.we_o := we
    val instant_data = RegInit(0.U(DATA_WIDTH.W))
    val output_data = RegInit(0.U(DATA_WIDTH.W))
    io_wb.dat_o := output_data

    // data，长为16的Vec，每个元素为一个8位数据，初始化为0
    val data = RegInit(VecInit(Seq.fill(64)(0.U(8.W))))
    val uart_addr = 0x10000000.U(32.W)
    val uart_cond = 0x10000005.U(32.W)

    switch (state) {
        is (read_wait_prepare) {  // 0
            state := read_wait_action
            we := false.B
            cyc := true.B
            stb := true.B
            sel := "b0010".U(4.W)
            addr := uart_cond
        }
        is (read_wait_action) { // 1
            when (io_wb.ack_i) {
                state := read_wait_check
                cyc := false.B
                stb := false.B
                addr := 0.U
                instant_data := io_wb.dat_i
            }.otherwise {
                state := read_wait_action
            }
        }
        is (read_wait_check) { // 2
            when (instant_data(8) === 1.U){
                state := read_data_action
                cyc := true.B
                stb := true.B
                addr := uart_addr
                sel := "b0001".U(4.W)
                we := false.B
            }.otherwise{
                state := read_wait_prepare
            }
        }
        is (read_data_action) { // 3
            when(io_wb.ack_i){
                state := read_data_done
                cyc := false.B
                stb := false.B
                addr := 0.U
                data(round) := io_wb.dat_i(7, 0)
                round := round + 1.U
            }.otherwise{
                state := read_data_action
            }
        }
        is (read_data_done) { // 4
            when(round >= 10.U) {
                state := write_sram_prepare
                round := 0.U         
            }.otherwise {
                state := read_wait_prepare
            }
        }
        is (write_sram_prepare){ // 5
            state := write_sram_action
            cyc := true.B
            stb := true.B
            output_data := Cat(0x000000.U(24.W), data(round))
            sel := "b0001".U(4.W)
            we := true.B
            addr := io.dip_sw + (round << 2.U)
        }
        is (write_sram_action) { // 6
            when(io_wb.ack_i){
                state := write_sram_done
                cyc := false.B
                stb := false.B
                round := round + 1.U
            }.otherwise{
                state := write_sram_action
            }
        }
        is (write_sram_done) { // 7
            when(round >= 10.U) {
                state := write_wait_prepare
                round := 0.U         
            }.otherwise {
                state := write_sram_prepare
            }
        }
        is (write_wait_prepare) { // 8
            state := write_wait_action
            cyc := true.B
            stb := true.B
            addr := uart_cond
            sel := "b0010".U(4.W)
            we := false.B
        }
        is (write_wait_action) { // 9
            when(io_wb.ack_i){
                state := write_wait_check
                cyc := false.B
                stb := false.B
                addr := 0.U
                instant_data := io_wb.dat_i
            }.otherwise{
                state := write_wait_action
            }        
        }
        is (write_wait_check) {  // a
            when (instant_data(13) === 1.U){ 
                state := write_data_action
                cyc := true.B
                stb := true.B
                addr := uart_addr
                sel := "b0001".U(4.W)
                we := true.B
                output_data := Cat(0x000000.U(24.W), data(round))
            }.otherwise{
                state := write_wait_prepare
            }
        }
        is (write_data_action) {  // b
            when(io_wb.ack_i){
                state := write_data_done
                cyc := false.B
                stb := false.B
                addr := 0.U
                round := round + 1.U
            }.otherwise{
                state := write_data_action
            }
        }
        is (write_data_done) {  // c
            when(round >= 10.U) {
                state := write_data_done
            }.otherwise {
                state := write_wait_prepare
            }
        }
    }
}