import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RdFPathTableDetailComponent } from './rd-f-path-table-detail.component';

describe('RdFPathTable Management Detail Component', () => {
  let comp: RdFPathTableDetailComponent;
  let fixture: ComponentFixture<RdFPathTableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RdFPathTableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ rdFPathTable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RdFPathTableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RdFPathTableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load rdFPathTable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.rdFPathTable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
