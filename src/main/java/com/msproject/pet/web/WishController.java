package com.msproject.pet.web;

import com.msproject.pet.entity.WishEntity;
import com.msproject.pet.model.Header;

import com.msproject.pet.service.WishService;
import com.msproject.pet.web.dtos.WishDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/wish")
@RestController
public class WishController {

    private final WishService wishService;

    @PostMapping("/wish")
    public WishEntity createWish(@RequestBody WishDto wishDto){
        if(checkId(wishDto.getUserNum(),wishDto.getPetHospitalNum())){
            return wishService.wishCreate(wishDto);
        }else{
            return update(wishDto);
        }
    }

    @GetMapping("/check/{uid}/{hid}")
    public Boolean checkId(@PathVariable Long uid, @PathVariable Long hid){
        return wishService.checkWish(uid,hid);
    }

    @PatchMapping("/wish")
    public WishEntity update(@RequestBody WishDto wishDto){
        return wishService.update(wishDto);
    }

    @DeleteMapping("/wish/{id}")
    public void delete(@PathVariable Long id){
        wishService.delete(id);
    }


    @GetMapping("/list/{uid}")
    public Header<List<WishDto>> wishList(@PathVariable Long uid,
                                          @PageableDefault(sort = {"idx"}) Pageable pageable)
    {
        return wishService.getWishList(pageable,uid);
    }

    @GetMapping("/{id}")
    public WishDto getWish(@PathVariable Long id){
        return wishService.getWish(id);
    }

    @GetMapping("/one/{uid}/{hid}")
    public WishDto getWish1(@PathVariable Long uid,@PathVariable Long hid){
        return wishService.getWish1(uid,hid);
    }

    @GetMapping("/change/{wid}")
    public WishEntity changeStateZero(@PathVariable Long wid){
        return  wishService.changeStateZero(wid);
    }
}
