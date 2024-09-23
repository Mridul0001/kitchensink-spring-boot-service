package com.kitchensink.ks.controller;

import com.kitchensink.ks.dto.Member;
import com.kitchensink.ks.exceptions.DuplicateMemberException;
import com.kitchensink.ks.exceptions.MemberException;
import com.kitchensink.ks.exceptions.MemberNotFoundException;
import com.kitchensink.ks.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestController to handle all member operations like fetch all, fetch by id, update, insert, delete
 * Base path is defined in application.properties file as /kitchensink/rest
 */
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Retrieve list of all registered members
     * @return List of members with their id, name, email, and phone
     */
    @GetMapping
    public ResponseEntity<List<Member>> getMembers() {
        return ResponseEntity.ok(memberService.getMembers());
    }

    /**
     * Fetch a member by id.
     * @param id id of the member to fetch
     * @return Member object with id, name, email and phone
     */
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable String id) throws MemberNotFoundException {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    /**
     * Register a new member
     * @param member Member object containing name, email, and phone
     * @return Member object with generated id along with name, email, and phone
     */
    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) throws DuplicateMemberException {
        return new ResponseEntity<>(memberService.createMember(member), HttpStatus.CREATED);
    }

    /**
     * Update a member
     * @param member Member object to update
     * @return Update success message
     */
    @PutMapping
    public ResponseEntity<Member> updateMember(@RequestBody Member member) throws MemberException {
        return new ResponseEntity<>(memberService.updateMember(member), HttpStatus.OK);
    }

    /**
     * Delete a member by id
     * @param id id of the member to delete
     * @return Deletion success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable String id) {
        memberService.deleteMemberById(id);
        return ResponseEntity.ok("Member deleted");
    }
}
