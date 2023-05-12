package com.shop.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.BoardImg;
import com.shop.repository.BoardImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardImgService {
    @Value("${boardImgLocation}")
    private String boardImgLocation;

    private final BoardImgRepository boardImgRepository;

    private final FileService fileService;

    public void saveBoardImg(BoardImg boardImg, MultipartFile boardImgFile) throws Exception{
        String oriImgName = boardImgFile.getOriginalFilename();
        String imgName="";
        String imgUrl="";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(boardImgLocation,oriImgName,boardImgFile.getBytes());
            imgUrl = "/img/board/" + imgName;
        }

        //상품 이미지 정보 저장
        boardImg.updateBoardImg(oriImgName,imgName,imgUrl);
        boardImgRepository.save(boardImg);

    }

    public void updateBoardImg(Long boardImgId, MultipartFile boardImgFile) throws Exception{
        if (!boardImgFile.isEmpty()){
            BoardImg savedBoardImg = boardImgRepository.findById(boardImgId)
                    .orElseThrow(EntityNotFoundException::new);
            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedBoardImg.getImgName())){
                fileService.deleteFile(boardImgLocation+"/"+savedBoardImg.getImgName());
            }
            String oriImgName = boardImgFile.getOriginalFilename();
            String imgName= fileService.uploadFile(boardImgLocation,oriImgName, boardImgFile.getBytes());
            String imgUrl="/img/board/"+imgName;
            savedBoardImg.updateBoardImg(oriImgName,imgName,imgUrl);
        }
    }
    
    // 특정 게시글 삭제
 	public void boardImgDelete(Long id) {
 		boardImgRepository.deleteByBoardId(id);
 	}
}
