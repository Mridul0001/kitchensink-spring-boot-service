package com.kitchensink.ks.controller;

import com.kitchensink.ks.dto.Member;
import com.kitchensink.ks.exceptions.DuplicateMemberException;
import com.kitchensink.ks.exceptions.MemberNotFoundException;
import com.kitchensink.ks.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MemberControllerTests {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMembers() {
        // Arrange
        Member member1 = new Member(){{
            setId("1");
            setName("John Doe");
            setEmail("john@example.com");
            setPhoneNumber("1234567890");
        }};
        Member member2 = new Member(){{
            setId("2");
            setName("Jane Doe");
            setEmail("jane@example.com");
            setPhoneNumber("987654321");
        }};
        List<Member> memberList = Arrays.asList(member1, member2);

        when(memberService.getMembers()).thenReturn(memberList);

        // Act
        ResponseEntity<List<Member>> response = memberController.getMembers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(memberService, times(1)).getMembers();
    }

    @Test
    void testGetMemberById() throws MemberNotFoundException {
        // Arrange
        String memberId = "1";
        Member member = new Member(){{
            setId("1");
            setName("John Doe");
            setEmail("john@example.com");
            setPhoneNumber("1234567890");
        }};

        when(memberService.getMemberById(memberId)).thenReturn(member);

        // Act
        ResponseEntity<Member> response = memberController.getMemberById(memberId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(member, response.getBody());
        verify(memberService, times(1)).getMemberById(memberId);
    }

    @Test
    void testCreateMember() throws DuplicateMemberException {
        // Arrange
        Member member = new Member(){{
            setName("John Doe");
            setEmail("john@example.com");
            setPhoneNumber("1234567890");
        }};

        when(memberService.createMember(member)).thenReturn(member);

        // Act
        ResponseEntity<Member> response = memberController.createMember(member);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(member, response.getBody());
        verify(memberService, times(1)).createMember(member);
    }

    @Test
    void testUpdateMember() throws Exception {
        // Arrange
        Member member = new Member(){{
            setId("1");
            setName("John Doe");
            setEmail("john@example.com");
            setPhoneNumber("1234567890");
        }};

        when(memberService.updateMember(member)).thenReturn(member);

        // Act
        ResponseEntity<Member> response = memberController.updateMember(member);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(member, response.getBody());
        verify(memberService, times(1)).updateMember(member);
    }

    @Test
    void testDeleteMember() {
        // Arrange
        String memberId = "1";

        doNothing().when(memberService).deleteMemberById(memberId);

        // Act
        ResponseEntity<String> response = memberController.deleteMember(memberId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Member deleted", response.getBody());
        verify(memberService, times(1)).deleteMemberById(memberId);
    }
}
