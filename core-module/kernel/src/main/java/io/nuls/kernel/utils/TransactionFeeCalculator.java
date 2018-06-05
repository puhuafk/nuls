/*
 * MIT License
 *
 * Copyright (c) 2017-2018 nuls.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.nuls.kernel.utils;

import io.nuls.kernel.model.Na;

/**
 * @author Niels
 * @date 2018/5/15
 */
public class TransactionFeeCalculator {

    public static final Na MIN_PRECE_PRE_1000_BYTES = Na.valueOf(1000000);
    public static final Na OTHER_PRECE_PRE_1000_BYTES = Na.valueOf(5000000);

    /**
     * 根据交易大小计算需要交纳的手续费
     * According to the transaction size calculate the handling fee.
     *
     * @param size 交易大小/size of the transaction
     */
    public static final Na getTransferFee(int size) {
        Na fee = MIN_PRECE_PRE_1000_BYTES.multiply(size / 1000);
        if (size % 1000 > 0) {
            fee = fee.add(MIN_PRECE_PRE_1000_BYTES);
        }
        return fee;
    }

    /**
     * 根据交易大小计算需要交纳的手续费
     * According to the transaction size calculate the handling fee.
     *
     * @param size 交易大小/size of the transaction
     */
    public static final Na getOtherFee(int size) {
        Na fee = OTHER_PRECE_PRE_1000_BYTES.multiply(size / 1000);
        if (size % 1000 > 0) {
            fee = fee.add(OTHER_PRECE_PRE_1000_BYTES);
        }
        return fee;
    }

    /**
     * 根据交易大小计算需要交纳的手续费
     * According to the transaction size calculate the handling fee.
     *
     * @param size 交易大小/size of the transaction
     */
    public static final Na getFee(int size, Na price) {
        if (price.isLessThan(MIN_PRECE_PRE_1000_BYTES)) {
            return Na.MAX;
        }
        Na fee = price.multiply(size / 1000);
        if (size % 1000 > 0) {
            fee = fee.add(price);
        }
        return fee;
    }
}
