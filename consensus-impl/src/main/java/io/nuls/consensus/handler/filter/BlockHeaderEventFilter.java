package io.nuls.consensus.handler.filter;

import io.nuls.consensus.event.BlockEvent;
import io.nuls.consensus.event.BlockHeaderEvent;
import io.nuls.event.bus.event.filter.NulsEventFilter;
import io.nuls.event.bus.event.filter.NulsEventFilterChain;

/**
 * @author Niels
 * @date 2017/12/6
 */
public class BlockHeaderEventFilter implements NulsEventFilter<BlockHeaderEvent> {
    @Override
    public void doFilter(BlockHeaderEvent event, NulsEventFilterChain chain) {
        //todo
        chain.doFilter(event);
    }
}
