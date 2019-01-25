package io.nuls.consensus.poc.storage.service;

import io.nuls.consensus.poc.storage.po.NextSeedPo;
import io.nuls.consensus.poc.storage.po.RandomSeedPo;

import java.util.List;

/**
 * @author Niels
 */
public interface RandomSeedsStorageService {

    NextSeedPo getNextSeed(byte[] address);

    boolean saveNextSeed(byte[] address, long nowHeight, byte[] nextSeed, byte[] seedHash);

    boolean saveRandomSeed(long height, byte[] seed, byte[] nextSeedHash);

    boolean deleteRandomSeed(long height);

    List<RandomSeedPo> getSeeds(long maxHeight, int seedCount);

    List<RandomSeedPo> getSeeds(long startHeight, long endHeight);
}
