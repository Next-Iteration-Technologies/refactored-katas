package movierental;

import movierental.formatters.TextStatementFormatter;
import movierental.movietypes.*;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.Assert.*;

/**
 * Characterization Tests for Movie Rental System
 * 
 * These tests document and verify the existing behavior of the system as a baseline.
 * They serve as regression tests to ensure refactoring doesn't change functionality.
 * 
 * Test Coverage Summary:
 * - Statement format and structure verification
 * - Cross-cutting behavior patterns
 * - System-wide edge cases
 */
public class CharacterizationTest {

    private final TextStatementFormatter formatter = new TextStatementFormatter();

    // ========== SYSTEM-WIDE BEHAVIOR CHARACTERIZATION ==========

    @Test
    @DisplayName("CHARACTERIZATION: Complete system behavior - pricing and points across all movie types")
    public void characterizeCompleteSystemBehavior() {
        Customer customer = new Customer("System Test");
        customer.addRental(new Rental(new RegularMovie("Regular 1 day"), 1));
        customer.addRental(new Rental(new RegularMovie("Regular 5 days"), 5));
        customer.addRental(new Rental(new NewReleaseMovie("New 1 day"), 1));
        customer.addRental(new Rental(new NewReleaseMovie("New 3 days"), 3));
        customer.addRental(new Rental(new ChildrensMovie("Children 1 day"), 1));
        customer.addRental(new Rental(new ChildrensMovie("Children 7 days"), 7));
        
        String statement = customer.generateStatement(new TextStatementFormatter());
        
        // Verify all charges
        assertTrue(statement.contains("2.0"));  // Regular 1 day
        assertTrue(statement.contains("6.5"));  // Regular 5 days
        assertTrue(statement.contains("3.0"));  // New 1 day OR Children 7 days base (needs disambiguation)
        assertTrue(statement.contains("9.0"));  // New 3 days
        assertTrue(statement.contains("1.5"));  // Children 1 day
        assertTrue(statement.contains("7.5"));  // Children 7 days
        
        // Total: 2.0 + 6.5 + 3.0 + 9.0 + 1.5 + 7.5 = 29.5
        assertTrue(statement.contains("Amount owed is 29.5"));
        
        // Points: 1 + 1 + 1 + 2 + 1 + 1 = 7
        assertTrue(statement.contains("You earned 7 frequent renter points"));
    }

    
    // ========== STATEMENT FORMAT CHARACTERIZATION ==========
    
    @Test
    @DisplayName("CHARACTERIZATION: Statement format structure and conventions")
    public void characterizeStatementFormat() {
        Customer customer = new Customer("Alice");
        customer.addRental(new Rental(new RegularMovie("Movie"), 3));
        
        String statement = customer.generateStatement(new TextStatementFormatter());
        
        // Verify header format
        assertTrue(statement.startsWith("Rental Record for Alice\n"));
        
        // Verify rental line format (tab, title, tab, amount, newline)
        assertTrue(statement.contains("\tMovie\t3.5\n"));
        
        // Verify footer format (no space before newline)
        assertTrue(statement.contains("Amount owed is 3.5\nYou earned"));
        assertTrue(statement.contains("You earned 1 frequent renter points"));
        
        // Verify no plural handling ("1 frequent renter points" not "1 frequent renter point")
        assertFalse(statement.contains("1 frequent renter point\n"));
    }

    @Test
    @DisplayName("CHARACTERIZATION: Decimal formatting always shows one decimal place")
    public void characterizeDecimalFormatting() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie("M1"), 3));  // 3.5
        customer.addRental(new Rental(new NewReleaseMovie("M2"), 2));  // 6.0
        
        String statement = customer.generateStatement(new TextStatementFormatter());
        
        // Individual charges formatted with one decimal place
        assertTrue(statement.contains("\tM1\t3.5\n"));
        assertTrue(statement.contains("\tM2\t6.0\n"));
        
        // Total formatted with one decimal place
        assertTrue(statement.contains("Amount owed is 9.5"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Statement preserves rental order")
    public void characterizeRentalOrdering() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie("First"), 1));
        customer.addRental(new Rental(new ChildrensMovie("Second"), 1));
        customer.addRental(new Rental(new NewReleaseMovie("Third"), 1));
        
        String statement = customer.generateStatement(new TextStatementFormatter());
        
        // Verify rentals appear in the order they were added
        int firstPos = statement.indexOf("First");
        int secondPos = statement.indexOf("Second");
        int thirdPos = statement.indexOf("Third");
        
        assertTrue(firstPos < secondPos);
        assertTrue(secondPos < thirdPos);
    }
    
    // ========== EDGE CASES CHARACTERIZATION ==========
    
    @Test
    @DisplayName("CHARACTERIZATION: Zero days rental charges base rate (except new releases)")
    public void characterizeZeroDaysRental() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie("R"), 0));
        customer.addRental(new Rental(new ChildrensMovie("C"), 0));
        customer.addRental(new Rental(new NewReleaseMovie("N"), 0));
        
        String statement = customer.generateStatement(new TextStatementFormatter());
        
        // Regular: $2.0 (base), Children's: $1.5 (base), New Release: $0.0 = $3.5 total
        assertTrue(statement.contains("Amount owed is 3.5"));
        
        // All earn 1 point (new release 0 days doesn't qualify for bonus)
        assertTrue(statement.contains("You earned 3 frequent renter points"));
    }

    
    @Test
    @DisplayName("CHARACTERIZATION: Special characters in customer and movie names are preserved")
    public void characterizeSpecialCharacterHandling() {
        Customer customer = new Customer("Mary Jane-Watson");
        customer.addRental(new Rental(new RegularMovie("Test: Movie & More!"), 1));
        customer.addRental(new Rental(new ChildrensMovie("Movie's Title"), 1));
        
        String statement = customer.generateStatement(new TextStatementFormatter());
        
        assertTrue(statement.contains("Rental Record for Mary Jane-Watson\n"));
        assertTrue(statement.contains("Test: Movie & More!"));
        assertTrue(statement.contains("Movie's Title"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Pricing model comparison across movie types")
    public void characterizePricingModelDifferences() {
        // Same rental period, different prices
        Movie regular = new RegularMovie("Regular");
        Movie newRelease = new NewReleaseMovie("New");
        Movie childrens = new ChildrensMovie("Children");
        
        // At 5 days:
        // Regular: $2.0 + (5-2)*$1.5 = $6.5
        // New Release: 5*$3.0 = $15.0
        // Children's: $1.5 + (5-3)*$1.5 = $4.5
        
        assertEquals(6.5, regular.getCharge(5), 0.01);
        assertEquals(15.0, newRelease.getCharge(5), 0.01);
        assertEquals(4.5, childrens.getCharge(5), 0.01);
        
        // New releases are always most expensive
        assertTrue(newRelease.getCharge(5) > regular.getCharge(5));
        assertTrue(newRelease.getCharge(5) > childrens.getCharge(5));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Frequent renter points only vary by movie type and duration")
    public void characterizeFrequentRenterPointsRules() {
        // Points are independent of price
        // Regular and Children's always give 1 point regardless of duration
        // New releases give 2 points if rented more than 1 day, otherwise 1 point
        
        assertEquals(1, new Rental(new RegularMovie("R"), 1).getFrequentRenterPoints());
        assertEquals(1, new Rental(new RegularMovie("R"), 100).getFrequentRenterPoints());
        
        assertEquals(1, new Rental(new ChildrensMovie("C"), 1).getFrequentRenterPoints());
        assertEquals(1, new Rental(new ChildrensMovie("C"), 100).getFrequentRenterPoints());
        
        assertEquals(1, new Rental(new NewReleaseMovie("N"), 1).getFrequentRenterPoints());
        assertEquals(2, new Rental(new NewReleaseMovie("N"), 2).getFrequentRenterPoints());
        assertEquals(2, new Rental(new NewReleaseMovie("N"), 100).getFrequentRenterPoints());
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Customer can have zero rentals")
    public void characterizeEmptyCustomer() {
        Customer customer = new Customer("Empty Customer");
        
        String expected = "Rental Record for Empty Customer\n" +
                "Amount owed is 0.0\n" +
                "You earned 0 frequent renter points";
        
        assertEquals(expected, customer.generateStatement(new TextStatementFormatter()));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Customer name is immutable and always reflected in statement")
    public void characterizeCustomerNameBehavior() {
        Customer customer1 = new Customer("John Smith");
        Customer customer2 = new Customer("123");
        Customer customer3 = new Customer("");
        
        assertEquals("John Smith", customer1.getName());
        assertTrue(customer1.generateStatement(new TextStatementFormatter()).contains("Rental Record for John Smith\n"));
        
        assertEquals("123", customer2.getName());
        assertTrue(customer2.generateStatement(new TextStatementFormatter()).contains("Rental Record for 123\n"));
        
        assertEquals("", customer3.getName());
        assertTrue(customer3.generateStatement(new TextStatementFormatter()).contains("Rental Record for \n"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Rental calculation delegates to movie type")
    public void characterizeRentalDelegationPattern() {
        // Rental doesn't calculate charges; it delegates to the Movie
        Rental regularRental = new Rental(new RegularMovie("R"), 5);
        Rental newReleaseRental = new Rental(new NewReleaseMovie("N"), 5);
        Rental childrensRental = new Rental(new ChildrensMovie("C"), 5);
        
        // Verify charge delegation works correctly
        assertEquals(6.5, regularRental.getCharge(), 0.01);
        assertEquals(15.0, newReleaseRental.getCharge(), 0.01);
        assertEquals(4.5, childrensRental.getCharge(), 0.01);
        
        // Verify statement line format
        assertEquals("\tR\t6.5\n", formatter.formatLine(regularRental));
        assertEquals("\tN\t15.0\n", formatter.formatLine(newReleaseRental));
        assertEquals("\tC\t4.5\n", formatter.formatLine(childrensRental));
    }
    
    // ========== ADDITIONAL EDGE CASES ==========
    
    @Test
    @DisplayName("CHARACTERIZATION: Very large rental periods - individual charges")
    public void characterizeVeryLargeRentalPeriodIndividualCharges() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie("R"), 100));
        customer.addRental(new Rental(new NewReleaseMovie("N"), 100));
        customer.addRental(new Rental(new ChildrensMovie("C"), 100));

        String statement = customer.generateStatement(formatter);

        assertTrue(statement.contains("149.0"));
        assertTrue(statement.contains("300.0"));
        assertTrue(statement.contains("147.0"));
    }

    @Test
    @DisplayName("CHARACTERIZATION: Very large rental periods - totals")
    public void characterizeVeryLargeRentalPeriodTotals() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie("R"), 100));
        customer.addRental(new Rental(new NewReleaseMovie("N"), 100));
        customer.addRental(new Rental(new ChildrensMovie("C"), 100));

        String statement = customer.generateStatement(formatter);

        assertTrue(statement.contains("Amount owed is 596.0"));
        assertTrue(statement.contains("You earned 4 frequent renter points"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Multiple rentals of same movie should work independently")
    public void characterizeMultipleRentalsOfSameMovie() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie("Same Movie"), 1));
        customer.addRental(new Rental(new RegularMovie("Same Movie"), 3));
        customer.addRental(new Rental(new RegularMovie("Same Movie"), 5));
        
        String statement = customer.generateStatement(formatter);
        
        // Should list "Same Movie" three times
        int count = statement.split("Same Movie", -1).length - 1;
        assertEquals(3, count);
        
        // Total: 2.0 + 3.5 + 6.5 = 12.0
        assertTrue(statement.contains("Amount owed is 12.0"));
        
        // Points: 1 + 1 + 1 = 3
        assertTrue(statement.contains("You earned 3 frequent renter points"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Empty string movie title should work")
    public void characterizeEmptyMovieTitle() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie(""), 1));
        
        String statement = customer.generateStatement(formatter);
        
        // Should contain empty title with proper formatting
        assertTrue(statement.contains("\t\t2.0"));
        assertTrue(statement.contains("Amount owed is 2.0"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Very long movie and customer names should work")
    public void characterizeVeryLongNames() {
        String longName = "This Is An Extremely Long Customer Name With Many Words To Test Edge Cases";
        String longMovieTitle = "This Is An Extremely Long Movie Title That Contains Many Words And Should Still Work Correctly In The Statement";
        
        Customer customer = new Customer(longName);
        customer.addRental(new Rental(new RegularMovie(longMovieTitle), 1));
        
        String statement = customer.generateStatement(formatter);
        
        assertTrue(statement.contains(longName));
        assertTrue(statement.contains(longMovieTitle));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Movie titles with tabs and newlines should be preserved")
    public void characterizeMovieTitlesWithWhitespace() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie("Movie\tWith\tTabs"), 1));
        
        String statement = customer.generateStatement(formatter);
        
        assertTrue(statement.contains("Movie\tWith\tTabs"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Text formatter calculates correct totals")
    public void characterizeTextFormatterCalculation() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie("Movie A"), 3));
        customer.addRental(new Rental(new NewReleaseMovie("Movie B"), 2));

        String statement = customer.generateStatement(new TextStatementFormatter());

        assertTrue(statement.contains("Amount owed is 9.5"));
        assertTrue(statement.contains("You earned 3 frequent renter points"));
    }

    @Test
    @DisplayName("CHARACTERIZATION: HTML formatter calculates correct totals")
    public void characterizeHtmlFormatterCalculation() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new RegularMovie("Movie A"), 3));
        customer.addRental(new Rental(new NewReleaseMovie("Movie B"), 2));

        String statement = customer.generateStatement(new movierental.formatters.HtmlStatementFormatter());

        assertTrue(statement.contains("<em>9.5</em>"));
        assertTrue(statement.contains("<em>3</em> frequent renter points"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Single rental with maximum points bonus")
    public void characterizeSingleRentalMaxPoints() {
        Customer customer = new Customer("Test");
        customer.addRental(new Rental(new NewReleaseMovie("Blockbuster"), 10));
        
        String statement = customer.generateStatement(formatter);
        
        // New release for 10 days: 10 * 3.0 = 30.0
        assertTrue(statement.contains("Amount owed is 30.0"));
        
        // Should earn 2 points (bonus for > 1 day)
        assertTrue(statement.contains("You earned 2 frequent renter points"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Mix of boundary and non-boundary rentals")
    public void characterizeMixOfBoundaryRentals() {
        Customer customer = new Customer("Test");
        // Regular at boundary (2 days)
        customer.addRental(new Rental(new RegularMovie("R Boundary"), 2));
        // Regular over boundary (3 days)
        customer.addRental(new Rental(new RegularMovie("R Over"), 3));
        // Children at boundary (3 days)
        customer.addRental(new Rental(new ChildrensMovie("C Boundary"), 3));
        // Children over boundary (4 days)
        customer.addRental(new Rental(new ChildrensMovie("C Over"), 4));
        // New Release at bonus boundary (1 day - no bonus)
        customer.addRental(new Rental(new NewReleaseMovie("N No Bonus"), 1));
        // New Release over bonus boundary (2 days - bonus)
        customer.addRental(new Rental(new NewReleaseMovie("N Bonus"), 2));
        
        String statement = customer.generateStatement(formatter);
        
        // Regular: 2.0 + 3.5 = 5.5
        // Children: 1.5 + 3.0 = 4.5
        // New Release: 3.0 + 6.0 = 9.0
        // Total: 19.0
        assertTrue(statement.contains("Amount owed is 19.0"));
        
        // Points: 1 + 1 + 1 + 1 + 1 + 2 = 7
        assertTrue(statement.contains("You earned 7 frequent renter points"));
    }
    
    @Test
    @DisplayName("CHARACTERIZATION: Numeric customer name should work")
    public void characterizeNumericCustomerName() {
        Customer customer = new Customer("12345");
        customer.addRental(new Rental(new RegularMovie("Movie"), 1));
        
        String statement = customer.generateStatement(formatter);
        
        assertTrue(statement.contains("Rental Record for 12345"));
    }
}
