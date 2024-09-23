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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getMembers() {
        List<MemberDocument> members = memberRepository.findAll();
        return MAPPER.convertValue(members, new TypeReference<List<Member>>() {});
    }

    /**
     *
     * @param id id of the member to fetch
     * @return Member object matching the given id
     * @throws MemberNotFoundException if member with the given id doesn't exist
     */
    public Member getMemberById(String id) throws MemberNotFoundException {
        MemberDocument memberDocument = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        return MAPPER.convertValue(memberDocument, Member.class);
    }

    public void deleteMemberById(String id){
        memberRepository.deleteById(id);
    }

    /**
     * Add new member into database
     * @param member Member object to insert into DB
     * @return Member object with newly created id
     * @throws DuplicateMemberException if a member already exists
     */
    public Member createMember(Member member) throws DuplicateMemberException {
        try {
            MemberDocument memberDocument = MAPPER.convertValue(member, MemberDocument.class);
            memberDocument = memberRepository.insert(memberDocument);
            return MAPPER.convertValue(memberDocument, Member.class);
        }catch (DuplicateKeyException e){
            throw new DuplicateMemberException();
        }
    }

    /**
     * Update an existing member
     * @param member Member object to update in DB
     * @return Updated member object
     * @throws MemberException if id is not present in request or duplicate key is found
     */
    public Member updateMember(Member member) throws MemberException {
        try {
            //id is mandatory. Throw MissingIdException if not present
            if(Strings.isBlank(member.getId())) throw new MissingIdException();
            MemberDocument memberDocument = MAPPER.convertValue(member, MemberDocument.class);
            memberDocument = memberRepository.save(memberDocument);
            return MAPPER.convertValue(memberDocument, Member.class);
        }catch (DuplicateKeyException e){
            throw new DuplicateMemberException();
        }
    }
}
