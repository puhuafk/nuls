/*
 * *
 *  * MIT License
 *  *
 *  * Copyright (c) 2017-2019 nuls.io
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package io.nuls.kernel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.nuls.kernel.context.NulsContext;
import io.nuls.kernel.exception.NulsException;
import io.nuls.kernel.script.Script;
import io.nuls.kernel.script.SignatureUtil;
import io.nuls.kernel.utils.NulsByteBuffer;
import io.nuls.kernel.utils.NulsOutputStreamBuffer;
import io.nuls.kernel.utils.SerializeUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author ln
 */
public class CoinData extends BaseNulsData {

    private List<Coin> from;

    private List<Coin> to;

    public CoinData() {
        from = new ArrayList<>();
        to = new ArrayList<>();
    }

    /**
     * serialize important field
     */
    @Override
    protected void serializeToStream(NulsOutputStreamBuffer stream) throws IOException {
        int fromCount = from == null ? 0 : from.size();
        stream.writeVarInt(fromCount);
        if (null != from) {
            for (Coin coin : from) {
                stream.writeNulsData(coin);
            }
        }
        int toCount = to == null ? 0 : to.size();
        stream.writeVarInt(toCount);
        if (null != to) {
            for (Coin coin : to) {
                stream.writeNulsData(coin);
            }
        }
    }

    @Override
    public void parse(NulsByteBuffer byteBuffer) throws NulsException {
        int fromCount = (int) byteBuffer.readVarInt();

        if (0 < fromCount) {
            List<Coin> from = new ArrayList<>();
            for (int i = 0; i < fromCount; i++) {
                from.add(byteBuffer.readNulsData(new Coin()));
            }
            this.from = from;
        }

        int toCount = (int) byteBuffer.readVarInt();

        if (0 < toCount) {
            List<Coin> to = new ArrayList<>();
            for (int i = 0; i < toCount; i++) {
                to.add(byteBuffer.readNulsData(new Coin()));
            }
            this.to = to;
        }
    }

    @Override
    public int size() {
        int size = SerializeUtils.sizeOfVarInt(from == null ? 0 : from.size());
        if (null != from) {
            for (Coin coin : from) {
                size += SerializeUtils.sizeOfNulsData(coin);
            }
        }
        size += SerializeUtils.sizeOfVarInt(to == null ? 0 : to.size());
        if (null != to) {
            for (Coin coin : to) {
                size += SerializeUtils.sizeOfNulsData(coin);
            }
        }
        return size;
    }

    public List<Coin> getFrom() {
        return from;
    }

    public void setFrom(List<Coin> from) {
        this.from = from;
    }

    public List<Coin> getTo() {
        return to;
    }

    public void setTo(List<Coin> to) {
        this.to = to;
    }

    /**
     * 获取该交易的手续费
     * The handling charge for the transaction.
     *
     * @return tx fee
     */
    @JsonIgnore
    public Na getFee() {
        Na toNa = Na.ZERO;
        for (Coin coin : to) {
            toNa = toNa.add(coin.getNa());
        }
        Na fromNa = Na.ZERO;
        for (Coin coin : from) {
            fromNa = fromNa.add(coin.getNa());
        }
        return fromNa.subtract(toNa);
    }

    public void addTo(Coin coin) {
        if (null == to) {
            to = new ArrayList<>();
        }
        if(coin.getOwner().length == 23 && coin.getOwner()[2] == NulsContext.P2SH_ADDRESS_TYPE){
            Script scriptPubkey = SignatureUtil.createOutputScript(coin.getOwner());
            coin.setOwner(scriptPubkey.getProgram());
        }
        to.add(coin);
    }

    public void addFrom(Coin coin) {
        if (null == from) {
            from = new ArrayList<>();
        }
        from.add(coin);
    }

    @JsonIgnore
    public Set<byte[]> getAddresses() {
        Set<byte[]> addressSet = new HashSet<>();
        if (to != null && to.size() != 0) {
            for (int i = 0; i < to.size(); i++) {
                byte[] owner = to.get(i).getAddress();
                boolean hasExist = false;
                for (byte[] address : addressSet) {
                    //todo 20180919 这里处理脚本的情况
                    if (Arrays.equals(owner, address)) {
                        hasExist = true;
                        break;
                    }
                }
                if (!hasExist) {
                    addressSet.add(owner);
                }
            }
        }
        return addressSet;
    }
}