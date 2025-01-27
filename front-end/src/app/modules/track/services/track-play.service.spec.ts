import { TestBed } from '@angular/core/testing';

import { TrackPlayService } from './track-play.service';

describe('TrackPlayService', () => {
  let service: TrackPlayService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrackPlayService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
