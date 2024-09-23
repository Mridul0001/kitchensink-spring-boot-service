package com.kitchensink.ks.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchensink.ks.data.documents.MemberDocument;
import com.kitchensink.ks.data.repository.MemberRepository;
import com.kitchensink.ks.dto.Member;
import com.kitchensink.ks.exceptions.DuplicateMemberException;
import com.kitchensink.ks.exceptions.MemberException;
import com.kitchensink.ks.exceptions.MemberNotFoundException;
import com.kitchensink.ks.exceptions.MissingIdException;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTests {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ObjectMapper();
    }

    @Test
    void testGetMembers() {
        when(memberRepository.findAll()).thenReturn(Collections.singletonList(new MemberDocument(){{
            setName("John Doe");
            setEmail("john.doe@example.com");
            setPhoneNumber("1234567890");
        }}));

        List<Member> members = memberService.getMembers();

        assertEquals(1, members.size());
        assertEquals("John Doe", members.get(0).getName());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testGetMemberById_Success() throws MemberNotFoundException {
        MemberDocument memberDocument = new MemberDocument(){{
            setId("1");
            setName("John Doe");
            setEmail("john.doe@example.com");
            setPhoneNumber("1234567890");
        }};
        when(memberRepository.findById("1")).thenReturn(Optional.of(memberDocument));

        Member member = memberService.getMemberById("1");

        assertNotNull(member);
        assertEquals("John Doe", member.getName());
        verify(memberRepository, times(1)).findById("1");
    }

    @Test
    void testGetMemberById_NotFound() {
        when(memberRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.getMemberById("1"));
    }

    @Test
    void testCreateMember_Success() throws DuplicateMemberException {
        Member member = new Member(){{
            setId("1");
            setName("John Doe");
            setEmail("john.doe@example.com");
            setPhoneNumber("1234567890");
        }};
        when(memberRepository.insert(any(MemberDocument.class))).thenReturn(new MemberDocument(){{
            setId("1");
            setName("John Doe");
            setEmail("john.doe@example.com");
            setPhoneNumber("1234567890");
        }});

        Member createdMember = memberService.createMember(member);

        assertNotNull(createdMember);
        assertEquals("John Doe", createdMember.getName());
        verify(memberRepository, times(1)).insert(any(MemberDocument.class));
    }

    @Test
    void testCreateMember_Duplicate() {
        Member member = new Member(){{
            setId("1");
            setName("John Doe");
            setEmail("john.doe@example.com");
            setPhoneNumber("1234567890");
        }};
        when(memberRepository.insert(any(MemberDocument.class))).thenThrow(new DuplicateKeyException("Duplicate key"));

        assertThrows(DuplicateMemberException.class, () -> memberService.createMember(member));
    }

    @Test
    void testUpdateMember_Success() throws MemberException {
        Member member = new Member(){{
            setId("1");
            setName("John Doe");
            setEmail("john.doe@example.com");
        }};
        when(memberRepository.save(any(MemberDocument.class))).thenReturn(new MemberDocument(){{
            setId("1");
            setName("John Doe");
            setEmail("john.doe@example.com");
            setPhoneNumber("1234567890");
        }});

        Member updatedMember = memberService.updateMember(member);

        assertNotNull(updatedMember);
        assertEquals("John Doe", updatedMember.getName());
        verify(memberRepository, times(1)).save(any(MemberDocument.class));
    }

    @Test
    void testUpdateMember_NotFound() {
        Member member = new Member(){{
            setId("1");
            setName("John Doe");
            setEmail("john.doe@example.com");
            setPhoneNumber("1234567890");
        }};
        when(memberRepository.save(any(MemberDocument.class))).thenThrow(new DuplicateKeyException("Duplicate key"));

        assertThrows(DuplicateMemberException.class, () -> memberService.updateMember(member));
    }

    @Test
    void testUpdateMember_MissingId() {
        Member member = new Member(){{
            setName("John Doe");
            setEmail("john.doe@example.com");
            setPhoneNumber("1234567890");
        }};

        assertThrows(MissingIdException.class, () -> memberService.updateMember(member));
    }

    @Test
    void testDeleteMemberById() {
        memberService.deleteMemberById("1");
        verify(memberRepository, times(1)).deleteById("1");
    }
}
