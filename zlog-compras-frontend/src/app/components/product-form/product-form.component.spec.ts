import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductFormComponent } from './product-form.component';
import { ProductService } from '../../services/product.service';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';

describe('ProductFormComponent', () => {
  let component: ProductFormComponent;
  let fixture: ComponentFixture<ProductFormComponent>;
  let mockProductService: any;
  let mockRouter: any;

  beforeEach(async () => {
    // Crie mocks para os serviços para isolar o componente
    mockProductService = jasmine.createSpyObj('ProductService', ['createProduct']);
    mockRouter = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        ProductFormComponent,
        ReactiveFormsModule, // Necessário para testar o formulário reativo
        RouterTestingModule // Fornece uma versão de teste do Router
      ],
      providers: [
        // Fornece os mocks para os serviços
        { provide: ProductService, useValue: mockProductService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty values', () => {
    // Verifica se o formulário é criado com os valores iniciais corretos
    expect(component.productForm.get('codigo')?.value).toBe('');
    expect(component.productForm.get('nome')?.value).toBe('');
    expect(component.productForm.get('precoUnitario')?.value).toBe(null);
  });

  it('should disable the form initially due to invalid fields', () => {
    // O formulário deve estar inválido no início, pois todos os campos são obrigatórios
    expect(component.productForm.invalid).toBeTrue();
  });

  it('should call createProduct on submit and navigate on success', () => {
    // Preenche o formulário com dados válidos
    component.productForm.setValue({
      codigo: 'A123',
      codigoProduto: 'B456',
      nome: 'Teste Produto',
      descricao: 'Descrição do produto teste',
      unidadeMedida: 'UN',
      precoUnitario: 10.50,
      categoria: 'Categoria Teste',
      estoque: 100
    });

    // Simula uma resposta de sucesso do serviço
    mockProductService.createProduct.and.returnValue(of({}));

    // Chama o método onSubmit
    component.onSubmit();

    // Verifica se o serviço foi chamado e se a navegação ocorreu
    expect(mockProductService.createProduct).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/products']);
  });
});
