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
 */

package io.nuls.consensus.poc.manager;

import io.nuls.consensus.poc.cache.CacheLoader;
import io.nuls.consensus.poc.container.ChainContainer;
import io.nuls.consensus.poc.entity.Agent;
import io.nuls.consensus.poc.model.Chain;
import io.nuls.consensus.poc.entity.Deposit;
import io.nuls.core.chain.entity.Block;
import io.nuls.core.chain.entity.BlockHeader;

import java.util.List;

/**
 * Created by ln on 2018/4/13.
 */
public class CacheManager {

    private ChainManager chainManager;

    private CacheLoader cacheLoader = new CacheLoader();

    public CacheManager(ChainManager chainManager) {
        this.chainManager = chainManager;
    }

    public void load() {

        //TODO
        //load storage data to memory

        List<BlockHeader> blockHeaderList = cacheLoader.loadBlockHeaders(200);
        List<Block> blockList = cacheLoader.loadBlocks(8);
        List<Agent> agentList = cacheLoader.loadAgents();
        List<Deposit> depositList = cacheLoader.loadDepositList();

        Chain masterChain = new Chain();

        masterChain.setBlockHeaderList(blockHeaderList);
        masterChain.setBlockList(blockList);

        masterChain.setStartBlockHeader(blockList.get(0).getHeader());
        masterChain.setEndBlockHeader(blockList.get(blockList.size() - 1).getHeader());

//        masterChain.setAgentList(agentList);
        //TODO

        ChainContainer masterChainContainer = new ChainContainer(masterChain);

        chainManager.setMasterChain(masterChainContainer);

        chainManager.getRoundManager().calculationAndSet(masterChainContainer);
    }

    public void reload() {
        clear();
        load();
    }

    public void clear() {
        chainManager.clear();
    }

    public CacheLoader getCacheLoader() {
        return cacheLoader;
    }

    public void setCacheLoader(CacheLoader cacheLoader) {
        this.cacheLoader = cacheLoader;
    }
}
