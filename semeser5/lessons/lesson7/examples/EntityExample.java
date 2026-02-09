// Example: JPA Entity with relationships

@Entity
@Table(name = "currencies")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 3)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExchangeHistory> history = new ArrayList<>();

    // Default constructor required by JPA
    public Currency() {}

    public Currency(String code, String name, BigDecimal rate) {
        this.code = code;
        this.name = name;
        this.rate = rate;
    }

    // Helper method for bidirectional relationship
    public void addHistory(ExchangeHistory record) {
        history.add(record);
        record.setCurrency(this);
    }

    // Getters and setters...
}

// Example: Many-to-One side
@Entity
@Table(name = "exchange_history")
public class ExchangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private BigDecimal rate;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Constructors, getters, setters...
}
