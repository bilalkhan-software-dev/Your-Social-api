package com.yoursocial.controller;

import com.yoursocial.dto.ReelRequest;
import com.yoursocial.dto.ReelResponse;
import com.yoursocial.endpoint.ReelsControllerEndpoint;
import com.yoursocial.services.ReelService;
import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReelController implements ReelsControllerEndpoint {

    private final ReelService reelService;
    private final CommonUtil response;

    @Override
    public ResponseEntity<?> createReel(ReelRequest request) {

        boolean isReelCreated = reelService.createReel(request);
        if (!isReelCreated) {
            return response.createBuildResponseMessage("reels added successfully!", HttpStatus.CREATED);
        }

        return response.createErrorResponseMessage("reels added failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getAllReel() {

        List<ReelResponse> allCreatedReel = reelService.allCreatedReel();

        if (!CollectionUtils.isEmpty(allCreatedReel)) {
            return response.createBuildResponse(allCreatedReel, HttpStatus.OK);
        }

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> allUserReel() {

        List<ReelResponse> reelsOfTheLoggedInUser = reelService.getReelOfTheLoggedInUser();

        if (!CollectionUtils.isEmpty(reelsOfTheLoggedInUser)) {
            return response.createBuildResponse(reelsOfTheLoggedInUser, HttpStatus.OK);
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> deleteUserReel(Integer reelId) {

        reelService.deleteReelOfTheUser(reelId);

        return response.createBuildResponseMessage("reel deleted successfully!", HttpStatus.OK);
    }
}
