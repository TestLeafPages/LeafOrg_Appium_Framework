Feature: Automate LeafOrg App

Scenario: TC001_Login test using valid credential for Positive Scenario
    Given the user enter valied credential username as rajkumar@testleaf.com and password as Leaf@123
    When the user clicked login which is designed under the password test field
    Then Home related page opened