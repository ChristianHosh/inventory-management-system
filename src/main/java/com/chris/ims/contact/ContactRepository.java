package com.chris.ims.contact;

import com.chris.ims.entity.AbstractEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * The ContactRepository interface defines the operations that can be performed on the Contact entity.
 * The Contact entity represents a contact in the system, such as an employee or a customer.
 * The repository is used to interact with the database and perform operations such as searching for contacts,
 * retrieving a specific contact, and saving a new contact.
 */
@Repository
public interface ContactRepository extends AbstractEntityRepository<Contact> {

    /**
     * Searches for contacts based on a query string. The query string can contain any search terms,
     * and the results will include contacts whose name or phone number contain the search terms.
     *
     * @param query the search query
     * @param pageable the pagination information
     * @return a page of contacts that match the search query
     */
    @Query("SELECT c FROM Contact c WHERE c.type = 0 AND c.keyword LIKE CONCAT('%', :query, '%')")
    Page<Contact> searchQueryIsEmployee(String query, Pageable pageable);

    /**
     * Searches for contacts based on a query string. The query string can contain any search terms,
     * and the results will include contacts whose name or phone number contain the search terms.
     *
     * @param query the search query
     * @param pageable the pagination information
     * @return a page of contacts that match the search query
     */
    @Query("SELECT c FROM Contact c WHERE c.type = 1 AND c.keyword LIKE CONCAT('%', :query, '%')")
    Page<Contact> searchQueryIsCustomer(String query, Pageable pageable);

}