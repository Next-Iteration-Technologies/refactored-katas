package gildedrose.itemtypes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class BackstagePassTest {

    @Test
    @DisplayName("BackstagePass has correct name")
    void backstagePassHasCorrectName() {
        BackstagePass pass = new BackstagePass(15, 20);
        assertEquals("Backstage passes to a TAFKAL80ETC concert", pass.getName());
    }

    @Test
    @DisplayName("BackstagePass increases quality by 1 when sellIn > 10")
    void backstagePassIncreasesBy1WhenSellInGreaterThan10() {
        BackstagePass pass = new BackstagePass(15, 20);
        pass.updateQuality();
        
        assertEquals(14, pass.getSellIn());
        assertEquals(21, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass increases quality by 1 when sellIn = 11")
    void backstagePassIncreasesBy1WhenSellInEquals11() {
        BackstagePass pass = new BackstagePass(11, 20);
        pass.updateQuality();
        
        assertEquals(10, pass.getSellIn());
        assertEquals(21, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass increases quality by 2 when sellIn = 10")
    void backstagePassIncreasesBy2WhenSellInEquals10() {
        BackstagePass pass = new BackstagePass(10, 20);
        pass.updateQuality();
        
        assertEquals(9, pass.getSellIn());
        assertEquals(22, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass increases quality by 2 when sellIn between 6 and 10")
    void backstagePassIncreasesBy2WhenSellInBetween6And10() {
        BackstagePass pass = new BackstagePass(8, 20);
        pass.updateQuality();
        
        assertEquals(7, pass.getSellIn());
        assertEquals(22, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass increases quality by 2 when sellIn = 6")
    void backstagePassIncreasesBy2WhenSellInEquals6() {
        BackstagePass pass = new BackstagePass(6, 20);
        pass.updateQuality();
        
        assertEquals(5, pass.getSellIn());
        assertEquals(22, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass increases quality by 3 when sellIn = 5")
    void backstagePassIncreasesBy3WhenSellInEquals5() {
        BackstagePass pass = new BackstagePass(5, 20);
        pass.updateQuality();
        
        assertEquals(4, pass.getSellIn());
        assertEquals(23, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass increases quality by 3 when sellIn between 1 and 5")
    void backstagePassIncreasesBy3WhenSellInBetween1And5() {
        BackstagePass pass = new BackstagePass(3, 20);
        pass.updateQuality();
        
        assertEquals(2, pass.getSellIn());
        assertEquals(23, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass increases quality by 3 when sellIn = 1")
    void backstagePassIncreasesBy3WhenSellInEquals1() {
        BackstagePass pass = new BackstagePass(1, 20);
        pass.updateQuality();
        
        assertEquals(0, pass.getSellIn());
        assertEquals(23, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass quality drops to zero after concert")
    void backstagePassQualityDropsToZeroAfterConcert() {
        BackstagePass pass = new BackstagePass(0, 20);
        pass.updateQuality();
        
        assertEquals(-1, pass.getSellIn());
        assertEquals(0, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass quality stays zero after concert")
    void backstagePassQualityStaysZeroAfterConcert() {
        BackstagePass pass = new BackstagePass(-5, 0);
        pass.updateQuality();

        assertEquals(-6, pass.getSellIn());
        assertEquals(0, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass already expired with non-zero quality resets to zero")
    void backstagePassAlreadyExpiredResetsNonZeroQualityToZero() {
        BackstagePass pass = new BackstagePass(-5, 20);
        pass.updateQuality();

        assertEquals(-6, pass.getSellIn());
        assertEquals(0, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass quality never exceeds 50 with long sellIn")
    void backstagePassQualityNeverExceeds50WithLongSellIn() {
        BackstagePass pass = new BackstagePass(15, 50);
        pass.updateQuality();
        
        assertEquals(14, pass.getSellIn());
        assertEquals(50, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass quality caps at 50 when increasing by 2")
    void backstagePassQualityCapsAt50WhenIncreasingBy2() {
        BackstagePass pass = new BackstagePass(10, 49);
        pass.updateQuality();
        
        assertEquals(9, pass.getSellIn());
        assertEquals(50, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass quality caps at 50 when increasing by 3")
    void backstagePassQualityCapsAt50WhenIncreasingBy3() {
        BackstagePass pass = new BackstagePass(5, 48);
        pass.updateQuality();
        
        assertEquals(4, pass.getSellIn());
        assertEquals(50, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass with quality 0 increases properly")
    void backstagePassWithQuality0IncreasesProperly() {
        BackstagePass pass = new BackstagePass(15, 0);
        pass.updateQuality();
        
        assertEquals(14, pass.getSellIn());
        assertEquals(1, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass full lifecycle from 15 days")
    void backstagePassFullLifecycle() {
        BackstagePass pass = new BackstagePass(15, 20);
        
        // Days 15-11: +1 each day
        for (int i = 0; i < 5; i++) {
            pass.updateQuality();
        }
        assertEquals(10, pass.getSellIn());
        assertEquals(25, pass.getQuality());
        
        // Days 10-6: +2 each day
        for (int i = 0; i < 5; i++) {
            pass.updateQuality();
        }
        assertEquals(5, pass.getSellIn());
        assertEquals(35, pass.getQuality());
        
        // Days 5-1: +3 each day
        for (int i = 0; i < 5; i++) {
            pass.updateQuality();
        }
        assertEquals(0, pass.getSellIn());
        assertEquals(50, pass.getQuality());
        
        // After concert: 0
        pass.updateQuality();
        assertEquals(-1, pass.getSellIn());
        assertEquals(0, pass.getQuality());
    }

    @Test
    @DisplayName("BackstagePass transitions from phase 1 to phase 2")
    void backstagePassTransitionsFromPhase1ToPhase2() {
        BackstagePass pass = new BackstagePass(11, 20);
        
        pass.updateQuality();
        assertEquals(10, pass.getSellIn());
        assertEquals(21, pass.getQuality()); // +1
        
        pass.updateQuality();
        assertEquals(9, pass.getSellIn());
        assertEquals(23, pass.getQuality()); // +2
    }

    @Test
    @DisplayName("BackstagePass transitions from phase 2 to phase 3")
    void backstagePassTransitionsFromPhase2ToPhase3() {
        BackstagePass pass = new BackstagePass(6, 20);
        
        pass.updateQuality();
        assertEquals(5, pass.getSellIn());
        assertEquals(22, pass.getQuality()); // +2
        
        pass.updateQuality();
        assertEquals(4, pass.getSellIn());
        assertEquals(25, pass.getQuality()); // +3
    }

    @Test
    @DisplayName("BackstagePass transitions from phase 3 to expired")
    void backstagePassTransitionsFromPhase3ToExpired() {
        BackstagePass pass = new BackstagePass(1, 20);
        
        pass.updateQuality();
        assertEquals(0, pass.getSellIn());
        assertEquals(23, pass.getQuality()); // +3
        
        pass.updateQuality();
        assertEquals(-1, pass.getSellIn());
        assertEquals(0, pass.getQuality()); // drops to 0
    }
}
